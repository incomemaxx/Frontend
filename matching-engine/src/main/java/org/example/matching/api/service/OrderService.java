package org.example.matching.api.service;

import lombok.RequiredArgsConstructor;
import org.example.matching.Wallets.RiskManager;
import org.example.matching.Wallets.WalletService;
import org.example.matching.api.dto.MarketEvent;
import org.example.matching.api.dto.OrderMapper;
import org.example.matching.api.dto.OrderRequest;
import org.example.matching.api.dto.OrderResponse;
import org.example.matching.api.dto.enums.EventStatus;
import org.example.matching.journal.EventJournal;
import org.example.matching.matching.MatchingEngine;
import org.example.matching.model.Order;
import org.example.matching.model.Trade;
import org.example.matching.orderbook.OrderRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OrderService implements ApplicationContextAware {

    private final MarketDataService marketDataService;
    private final RiskManager riskManager;
    private final MatchingEngine matchingEngine;
    private final WalletService walletService;
    private final EventJournal eventJournal;
    private final OrderRepository orderRepository;
    private final MarketManagmentService marketManagmentService;
    
    private ApplicationContext applicationContext;
    private LiquidBotService liquidBotService;

    // In-memory idempotency store: Key -> Previous Response
    private final Map<String, OrderResponse> idempotencyStore = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    private LiquidBotService getLiquidBotService() {
        if (liquidBotService == null) {
            liquidBotService = applicationContext.getBean(LiquidBotService.class);
        }
        return liquidBotService;
    }

    public OrderResponse processOrder(OrderRequest request) {
        if (idempotencyStore.containsKey(request.getIdempotencyKey())) {
            return idempotencyStore.get(request.getIdempotencyKey());
        }
        
        Order order = OrderMapper.toDomain(request);
        
        // Check if event is settled FIRST (before any other validation)
        MarketEvent event = marketManagmentService.getEventByTicker(order.getInstrument());
        if (event != null && EventStatus.SETTLED.equals(event.getStatus())) {
            return buildResponse(order, "REJECTED", "Event is settled - no more orders allowed");
        }
        
        if (!riskManager.checkAndReserve(order)) {
            return buildResponse(order, "REJECTED", "Insufficient funds or shares");
        }

        orderRepository.save(order);
        eventJournal.appendRaw("ORDER_PLACED: " + order.getId());

        // Matching Engine execution
        List<Trade> trades = matchingEngine.placeOrder(order);

        for (Trade trade : trades) {
            // so once trade is done manage the cash and shares of the users using their ids and stuff in walletService below
            walletService.settleTrade(trade);
            marketDataService.UpdateTrade(order.getInstrument(),trade.getPrice(),trade.getQuantity());
            eventJournal.appendRaw("TRADE_SETTLED: " + trade.getBuyOrderId() + " <-> " + trade.getSellOrderId());
            
            // Debug: Log trade details
            System.out.println("DEBUG: Trade occurred - BuyID: " + trade.getBuyOrderId() + 
                             ", SellID: " + trade.getSellOrderId() + 
                             ", UserID: " + order.getUserId() + 
                             ", Side: " + order.getSide());
            
            // If trade involves HOUSE_BOT, record it for virtual counter
            if (order.getUserId().equals("HOUSE_BOT") || 
                (trade.getBuyOrderId().startsWith("HOUSE_BOT") || trade.getSellOrderId().startsWith("HOUSE_BOT"))) {
                System.out.println("DEBUG: Recording trade for virtual counter");
                getLiquidBotService().recordTrade(order.getInstrument(), trade.getQuantity(), order.getSide().name());
            } else {
                System.out.println("DEBUG: Trade does not involve HOUSE_BOT - skipping virtual counter");
            }
        }
        var book = matchingEngine.getOrderBookForMarketData(order.getInstrument());
        marketDataService.updateBookTops(
                order.getInstrument(),
                book.getBestBid(),
                book.getBestAsk()
        );

        OrderResponse response = buildResponse(order, "ACCEPTED", "Success");
        idempotencyStore.put(request.getIdempotencyKey(), response);
        return response;
    }
    
    private OrderResponse buildResponse(Order order, String status, String msg) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(status)
                .message(msg)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}

