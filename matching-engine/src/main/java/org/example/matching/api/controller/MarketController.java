package org.example.matching.api.controller;

import org.example.matching.api.dto.OrderBookResponse;
import org.example.matching.matching.MatchingEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {
    private final MatchingEngine matchingEngine;

    public MarketController(MatchingEngine matchingEngine) {
        this.matchingEngine = matchingEngine;
    }

    @GetMapping("/orderbook/{instrument}")
    public ResponseEntity<OrderBookResponse> getOrderBook(@PathVariable String instrument) {
        OrderBookResponse snapshot = matchingEngine.getSnapshot(instrument);
        return ResponseEntity.ok(snapshot);
    }
}
