package org.example.matching.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Data
@Slf4j
public class OrderBook {
    private final String instrument;
    private final TreeMap<Long, List<Order>> bids;  // Price -> Orders (descending for bids)
    private final TreeMap<Long, List<Order>> asks;  // Price -> Orders (ascending for asks)

    public OrderBook() {
        this.instrument = "";
        this.bids = new TreeMap<>((a, b) -> Long.compare(b, a));  // Descending
        this.asks = new TreeMap<>();  // Ascending
    }

    public OrderBook(String instrument) {
        this.instrument = instrument;
        this.bids = new TreeMap<>((a, b) -> Long.compare(b, a));  // Descending
        this.asks = new TreeMap<>();  // Ascending
    }

    public void addOrder(Order order) {
        if (order.getSide() == OrderSide.BUY) {
            bids.computeIfAbsent(order.getPrice(), k -> new ArrayList<>()).add(order);
        } else {
            asks.computeIfAbsent(order.getPrice(), k -> new ArrayList<>()).add(order);
        }
    }

    public List<Trade> matchBuy(Order buyOrder) {
        List<Trade> trades = new ArrayList<>();
        
        while (buyOrder.getQuantity() > 0 && !asks.isEmpty()) {
            Long bestAskPrice = asks.firstKey();
            if (bestAskPrice > buyOrder.getPrice()) {
                break; // No match available
            }

            List<Order> askOrders = asks.get(bestAskPrice);
            if (askOrders.isEmpty()) {
                asks.remove(bestAskPrice);
                continue;
            }

            Order askOrder = askOrders.get(0);
            int tradeQuantity = Math.min(buyOrder.getQuantity(), askOrder.getQuantity());
            
            // Create trade
            Trade trade = new Trade(buyOrder.getId(), askOrder.getId(), bestAskPrice, tradeQuantity, buyOrder.getInstrument());
            trades.add(trade);

            // Update quantities
            buyOrder.reduceQuantity(tradeQuantity);
            askOrder.reduceQuantity(tradeQuantity);

            // Remove filled orders
            if (askOrder.isFilled()) {
                askOrders.remove(0);
                if (askOrders.isEmpty()) {
                    asks.remove(bestAskPrice);
                }
            }
        }

        // Add remaining buy order if not filled
        if (buyOrder.getQuantity() > 0) {
            addOrder(buyOrder);
        }

        return trades;
    }

    public List<Trade> matchSell(Order sellOrder) {
        List<Trade> trades = new ArrayList<>();
        
        while (sellOrder.getQuantity() > 0 && !bids.isEmpty()) {
            Long bestBidPrice = bids.firstKey();
            if (bestBidPrice < sellOrder.getPrice()) {
                break; // No match available
            }

            List<Order> bidOrders = bids.get(bestBidPrice);
            if (bidOrders.isEmpty()) {
                bids.remove(bestBidPrice);
                continue;
            }

            Order bidOrder = bidOrders.get(0);
            int tradeQuantity = Math.min(sellOrder.getQuantity(), bidOrder.getQuantity());
            
            // Create trade
            Trade trade = new Trade(bidOrder.getId(), sellOrder.getId(), bestBidPrice, tradeQuantity, sellOrder.getInstrument());
            trades.add(trade);

            // Update quantities
            sellOrder.reduceQuantity(tradeQuantity);
            bidOrder.reduceQuantity(tradeQuantity);

            // Remove filled orders
            if (bidOrder.isFilled()) {
                bidOrders.remove(0);
                if (bidOrders.isEmpty()) {
                    bids.remove(bestBidPrice);
                }
            }
        }

        // Add remaining sell order if not filled
        if (sellOrder.getQuantity() > 0) {
            addOrder(sellOrder);
        }

        return trades;
    }

    public List<Order> cancelAllOrdersForUser(String userId) {
        List<Order> cancelledOrders = new ArrayList<>();
        
        // Cancel bids
        bids.values().forEach(orderList -> {
            orderList.removeIf(order -> {
                if (order.getUserId().equals(userId) || userId.equals("*")) {
                    cancelledOrders.add(order);
                    return true;
                }
                return false;
            });
        });
        
        // Remove empty price levels
        bids.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        
        // Cancel asks
        asks.values().forEach(orderList -> {
            orderList.removeIf(order -> {
                if (order.getUserId().equals(userId) || userId.equals("*")) {
                    cancelledOrders.add(order);
                    return true;
                }
                return false;
            });
        });
        
        // Remove empty price levels
        asks.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        
        return cancelledOrders;
    }

    public Double getBestBid() {
        return bids.isEmpty() ? 0.0 : bids.firstKey().doubleValue();
    }

    public Double getBestAsk() {
        return asks.isEmpty() ? 0.0 : asks.firstKey().doubleValue();
    }

    public String dumpBook() {
        StringBuilder sb = new StringBuilder();
        sb.append("BIDS:\n");
        for (Map.Entry<Long, List<Order>> entry : bids.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue().size()).append(" orders\n");
        }
        sb.append("ASKS:\n");
        for (Map.Entry<Long, List<Order>> entry : asks.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(": ").append(entry.getValue().size()).append(" orders\n");
        }
        return sb.toString();
    }
}
