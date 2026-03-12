package org.example.matching.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String id;
    private String questions;
    private String yesTicker;
    private String noTicker;
    private int expiry; // minutes from now
    private Long liquidity; // Optional - defaults to 10000 if not provided
}