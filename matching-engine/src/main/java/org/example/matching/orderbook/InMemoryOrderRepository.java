package org.example.matching.orderbook;

import org.example.matching.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@Repository
public class InMemoryOrderRepository implements OrderRepository {
    
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    
    public InMemoryOrderRepository() {
        // Constructor
    }

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public void deleteById(String id) {
        orders.remove(id);
    }

    @Override
    public void delete(Order order) {
        orders.remove(order.getId());
    }
}
