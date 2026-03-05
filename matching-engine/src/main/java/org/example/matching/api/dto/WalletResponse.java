package org.example.matching.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class WalletResponse {
    private String userId;
    private long availableCash;
    private long reservedCash;
    private Map<String, Long> availableShares;
    private Map<String, Long> reservedShares;
}

@Data
public
class DepositRequest {
    private String userId;
    private long amount;
    private String instrument; // Optional: used if depositing shares instead of cash
}