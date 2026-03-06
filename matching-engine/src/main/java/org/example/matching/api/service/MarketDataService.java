package org.example.matching.api.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MarketDataService {
    //stores last price each stock is traded at
    private final Map<String,Double> lastPrice = new ConcurrentHashMap<>();

    // Average V(volume)WAP  = sum(price*quantity)/sum(quantity)
    private final Map<String,Double> sumPV = new ConcurrentHashMap<>();
    private final Map<String,Long> sumV = new ConcurrentHashMap<>();
    // To track the "Top of Book" (Best Bid/Ask)
    private final Map<String, Double> bestBids = new ConcurrentHashMap<>();
    private final Map<String, Double> bestAsks = new ConcurrentHashMap<>();

    public void UpdateTrade(String instrument, long price , long quantity){
        lastPrice.put(instrument,(double)price);
        sumPV.merge(instrument,(double)(price*quantity),Double::sum);
        sumV.merge(instrument,quantity,Long::sum);
    }

    public void updateBookTops(String instrument, Double bestBid, Double bestAsk) {
        if (bestBid != null) bestBids.put(instrument, bestBid);
        if (bestAsk != null) bestAsks.put(instrument, bestAsk);
    }


    public Map<String , Object> getSnapshots(String instrument){
        double ltp = lastPrice.getOrDefault(instrument,0.0);
        double vwap = sumV.getOrDefault(instrument,0L)==0?0: sumPV.get(instrument)/ sumV.get(instrument);
        double bid = bestBids.getOrDefault(instrument, 0.0);
        double ask = bestAsks.getOrDefault(instrument, 0.0);
        double mid = (bid > 0 && ask > 0) ? (bid + ask) / 2.0 : ltp;

        return Map.of(
                "instrument", instrument,
                "lastPrice", ltp,
                "vwap", vwap,
                "bid", bid,
                "ask", ask,
                "mid", mid,
                "spread", (ask - bid)
        );
    }
}