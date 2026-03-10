package org.example.matching.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.matching.Wallets.WalletService;
import org.example.matching.api.dto.OrderRequest;
import org.example.matching.model.OrderSide;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiquidBotService {
    
    private final OrderService orderService;
    private final WalletService walletService;
    private final MarketManagmentService marketManagmentService;
    
    private static final String BOT_ID = "HOUSE_BOT";
    private static final long INITIAL_INVENTORY = 50000; // Starting YES shares
    private static final long LIQUIDITY_PARAMETER = 100000; // L parameter for sigmoid
    
    public void updateMarketTrigger(String instrument) {
        // Check if this is a YES contract (NO contracts will be handled automatically)
        if (!instrument.endsWith("_Y")) return;
        
        String yesTicker = instrument;
        String noTicker = instrument.replace("_Y", "_N");
        
        // Cancel existing orders for both sides
        cancelBotOrders(yesTicker);
        cancelBotOrders(noTicker);
        
        // Recalculate and place new orders
        recalculateAndPlaceOrders(yesTicker, noTicker, LIQUIDITY_PARAMETER);
    }
    
    private void recalculateAndPlaceOrders(String yesTicker, String noTicker, long L) {
        // Get current YES inventory
        long yesInventory = walletService.getWallet(BOT_ID)
                .getAvailableShares()
                .getOrDefault(yesTicker, new AtomicLong(0))
                .get();
        
        // Calculate net sold (positive = sold YES, negative = bought YES)
        long netSold = INITIAL_INVENTORY - yesInventory;
        
        // Kalshi sigmoid pricing: Price = 100 / (1 + e^(-netSold / L))
        double exponent = (double) netSold / L;
        long fairPriceYes = (long) (100.0 / (1.0 + Math.exp(-exponent)));
        
        // Clamp prices between 1 and 99
        fairPriceYes = Math.max(1, Math.min(99, fairPriceYes));
        long fairPriceNo = 100 - fairPriceYes;
        
        // Calculate spread (2 cents each side for 4 cent total spread)
        long yesBid = Math.max(1, fairPriceYes - 2);
        long yesAsk = Math.min(99, fairPriceYes + 2);
        long noBid = Math.max(1, fairPriceNo - 2);
        long noAsk = Math.min(99, fairPriceNo + 2);
        
        // Place new orders with 500 share quantity
        placeBotOrder(yesTicker, OrderSide.BUY, yesBid, 500);
        placeBotOrder(yesTicker, OrderSide.SELL, yesAsk, 500);
        placeBotOrder(noTicker, OrderSide.BUY, noBid, 500);
        placeBotOrder(noTicker, OrderSide.SELL, noAsk, 500);
        
        log.info("Bot updated prices for {}: YES {}-{}, NO {}-{} (inventory: {})", 
                yesTicker, yesBid, yesAsk, noBid, noAsk, yesInventory);
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
    
    private void cancelBotOrders(String instrument) {
        // This would need to be implemented to cancel existing bot orders
        // For now, we'll rely on the order service to handle idempotency
        log.info("Cancelling existing bot orders for {}", instrument);
    }
}