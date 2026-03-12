package org.example.matching.api.service;

import lombok.RequiredArgsConstructor;
import org.example.matching.Wallets.RiskManager;
import org.example.matching.Wallets.WalletService;
import org.example.matching.api.dto.MarketEvent;
import org.example.matching.api.dto.enums.EventStatus;
import org.example.matching.matching.MatchingEngine;
import org.example.matching.model.Order;
import org.example.matching.model.Wallet;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {
    private final WalletService walletService;
    private final MarketManagmentService marketService;
    private final RiskManager riskManager;
    private final MatchingEngine matchingEngine;


    public void settleEvent(String eventId, String winningOutcome){
        MarketEvent event = marketService.getEvent(eventId);
        if(event==null|| event.getStatus()== EventStatus.SETTLED) return;;

        // winningTicker will get assigned the outcome , if it is yes , the winningTicker is yes and no so winningTicker is No
        String winningTicker = winningOutcome.equalsIgnoreCase("YES")? event.getYesTicker():event.getNoTicker();

        //losingTicker will get assigned the outcome , if it is yes , the losingTicker is yes and no so losingTicker is NOx
        String losingTicker = winningOutcome.equalsIgnoreCase("YES")?event.getNoTicker():event.getYesTicker();

        Collection<Wallet> allWallets = walletService.getAllWallets();

        for (Wallet wallet : allWallets) {
            // 2. Pay out the Winners ($1.00 per share)

            //checking how many shares of yes one has
            long winningShares = wallet.getAvailableShares().containsKey(winningTicker)
                    ? wallet.getAvailableShares().get(winningTicker).get()
                    : 0L;            if (winningShares > 0) {
                long payout = winningShares * 100; // 100 cents = $1.00
                wallet.addAvailableCash(payout);
                wallet.getAvailableShares().remove(winningTicker);
            }

            // 3. Remove the Losing Shares (Worth $0)
            wallet.getAvailableShares().remove(losingTicker);
        }

        event.setStatus(EventStatus.SETTLED);
        event.setOutcome(winningOutcome);
        
        // Clear order books for both tickers after settlement
        refundTicker(event.getYesTicker());
        refundTicker(event.getNoTicker());
    }


    //check if walletService or RiskManager
    private void refundTicker(String ticker){
        List<Order>  orders = matchingEngine.clearBook(ticker);
        for (Order o:orders){
            walletService.releaseReservation(o.getId());
        }
    }
}