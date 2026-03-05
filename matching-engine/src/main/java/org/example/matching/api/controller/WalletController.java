package org.example.matching.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.matching.Wallets.WalletService;
import org.example.matching.api.dto.DepositRequest;
import org.example.matching.api.dto.WalletResponse;
import org.example.matching.model.Wallet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable String userId){
        Wallet wallet = walletService.getWallet(userId);

        WalletResponse response = WalletResponse.builder()
                .userId(userId)
                .availableCash(wallet.getAvailableCash())
                .reservedCash(wallet.getReservedCash())
                // In a real app, you'd map the AtomicLongs to Longs here
                .build();
        return ResponseEntity.ok(response);

    }
    @PostMapping("/depostiCash")
    public ResponseEntity<String> depositCash(@RequestBody DepositRequest request) {
        walletService.creditUserCash(request.getUserId(), request.getAmount());

        return ResponseEntity.ok("Deposit Successful");
    }
}