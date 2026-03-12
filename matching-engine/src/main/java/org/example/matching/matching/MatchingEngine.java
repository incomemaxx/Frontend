package org.example.matching.matching;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.matching.Wallets.WalletService;
import org.example.matching.api.dto.OrderBookResponse;
import org.example.matching.api.dto.PriceLevel;
import org.example.matching.journal.EventJournal;
import org.example.matching.model.Order;
import org.example.matching.model.OrderBook;
import org.example.matching.model.OrderSide;
import org.example.matching.model.Trade;
import org.example.matching.orderbook.OrderRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matching engine is responsible for matching orders and maintaining order books.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingEngine {

    private final OrderRepository orderRepository;
    private final WalletService walletService;
    private final EventJournal journal;

    private final Map<String, OrderBook> orderBooks = new HashMap<>();

    @PostConstruct
    public void init() {
        // Pre-create books for stocks you want to test
        orderBooks.put("AAPL", new OrderBook());
        orderBooks.put("TSLA", new OrderBook());
        orderBooks.put("BTC", new OrderBook());
    }

    public OrderBookResponse getSnapshot(String instrument) {
        OrderBook book = getOrderBook(instrument);
        return OrderBookResponse.builder()
                .instrument(instrument)
                .bids(getPriceLevels(book.getBids()))
                .asks(getPriceLevels(book.getAsks()))
                .build();
    }

    public OrderBook getOrderBookForMarketData(String instrument) {
        return orderBooks.getOrDefault(instrument, new OrderBook());
    }

    public List<Trade> placeOrder(Order order) {
        return placeOrder(order, true);
    }

    public List<Trade> placeOrder(Order order, boolean record) {
        if (record) {
            journal.appendOrder(order.getId(), order.getUserId(), order.getSide().name(), order.getPrice(), order.getQuantity(), order.getTimestamp());
        }

        OrderBook book = getOrderBook(order.getInstrument());
        List<Trade> trades;

        if (order.getSide() == OrderSide.BUY) {
            trades = book.matchBuy(order);
        } else {
            trades = book.matchSell(order);
        }

        // Record trades
        for (Trade trade : trades) {
            if (record) {
                journal.appendTrade(trade.getBuyOrderId(), trade.getSellOrderId(), trade.getPrice(), (int) trade.getQuantity(), trade.getTimestamp());
            }
        }

        return trades;
    }

    public List<Order> cancelAllOrdersForUser(String userId, String instrument) {
        List<Order> cancelledOrders = new ArrayList<>();
        OrderBook book = orderBooks.get(instrument);
        if (book != null) {
            cancelledOrders = book.cancelAllOrdersForUser(userId);
        }
        return cancelledOrders;
    }
    
    public List<Order> clearBook(String ticker) {
        List<Order> cancelledOrders = new ArrayList<>();
        OrderBook book = orderBooks.get(ticker);
        if (book != null) {
            cancelledOrders = book.cancelAllOrdersForUser("*"); // Cancel all orders
        }
        return cancelledOrders;
    }

    private OrderBook getOrderBook(String instrument) {
        return orderBooks.computeIfAbsent(instrument, k -> new OrderBook());
    }

    private List<PriceLevel> getPriceLevels(Map<Long, List<Order>> side) {
        return side.entrySet().stream()
                .map(entry -> {
                    long price = entry.getKey();
                    long totalQty = entry.getValue().stream()
                            .mapToLong(Order::getQuantity)
                            .sum();
                    return new PriceLevel(price, totalQty);
                })
                .toList();
    }
}
