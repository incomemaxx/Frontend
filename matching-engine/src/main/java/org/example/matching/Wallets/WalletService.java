package org.example.matching.Wallets;

import org.example.matching.model.Order;
import org.example.matching.model.Trade;

public interface WalletService {
    boolean reserveForOrder(Order order);
    void  releaseReservation(String Oderid);
    void settleTrade(Trade trade);


    }
