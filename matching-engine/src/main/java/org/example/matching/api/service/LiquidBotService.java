package org.example.matching.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.matching.api.dto.OrderRequest;
import org.example.matching.api.dto.OrderResponse;
import org.example.matching.api.dto.MarketEvent;
import org.example.matching.matching.MatchingEngine;
import org.example.matching.model.OrderSide;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiquidBotService {
    
    private final OrderService orderService;
    private final MarketManagmentService marketManagmentService;
    private final MatchingEngine matchingEngine;
    
    private static final String BOT_ID = "HOUSE_BOT";
    private static final int SPREAD = 4;

    public void updateMarketTrigger(String ticker) {
        MarketEvent event = marketManagmentService.getEventByTicker(ticker);
        if (event == null) return;

        // 1. Get the displacement (starts at 0)
        long netSold = event.getVirtualNetSold().get();
        long L = event.getLiquidity();

        // 2. SIGMOID MATH (Guaranteed 50¢ if netSold is 0)
        // Price = 100 / (1 + e^(-0 / L)) -> 100 / (1 + 1) = 50
        double exponent = (double) netSold / L;
        long fairPriceYes = (long) (100.0 / (1.0 + Math.exp(-exponent)));

        fairPriceYes = Math.max(1, Math.min(99, fairPriceYes));
        long fairPriceNo = 100 - fairPriceYes;

        // 3. Update the Order Book
        refreshBotOrders(event.getYesTicker(), fairPriceYes, true);
        refreshBotOrders(event.getNoTicker(), fairPriceNo, false);
        
        log.info("Bot updated prices for {}: YES {}¢, NO {}¢ (virtualNetSold: {})", 
                ticker, fairPriceYes, fairPriceNo, netSold);
    }

    // This is called by your OrderService AFTER a trade happens
    public void recordTrade(String ticker, long quantity, String side) {
        MarketEvent event = marketManagmentService.getEventByTicker(ticker);
        if (event == null) return;

        // If a human BUYS from the bot, displacement goes UP
        // If a human SELLS to the bot, displacement goes DOWN
        if (side.equalsIgnoreCase("BUY")) {
            event.getVirtualNetSold().addAndGet(quantity);
        } else {
            event.getVirtualNetSold().addAndGet(-quantity);
        }

        log.info("Recorded trade: {} {} {} shares (virtualNetSold: {})", 
                ticker, side, quantity, event.getVirtualNetSold().get());

        // Immediately trigger price update based on new displacement
        updateMarketTrigger(ticker);
    }

    private void refreshBotOrders(String ticker, long fairPrice, boolean isYesTicker) {
        // Cancel all existing bot orders for this ticker
        matchingEngine.cancelAllOrdersForUser(BOT_ID, ticker);

        // Place new orders with 2-cent spread around fair price
        long bidPrice = Math.max(1, fairPrice - 2);
        long askPrice = Math.min(99, fairPrice + 2);

        // Place bid order (buy from users)
        placeBotOrder(ticker, OrderSide.BUY, bidPrice, 500);
        
        // Place ask order (sell to users)
        placeBotOrder(ticker, OrderSide.SELL, askPrice, 500);
    }

    private void placeBotOrder(String ticker, OrderSide side, long price, long quantity) {
        OrderRequest req = new OrderRequest();
        req.setUserId(BOT_ID);
        req.setInstrument(ticker);
        req.setSide(side.name());
        req.setPrice(price);
        req.setQuantity(quantity);
        req.setIdempotencyKey(BOT_ID + "-" + ticker + "-" + side + "-" + System.currentTimeMillis());
        
        try {
            orderService.processOrder(req);
        } catch (Exception e) {
            log.error("Failed to place bot order: {}", req, e);
        }
    }
}