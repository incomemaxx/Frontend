package org.example.matching.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.matching.api.dto.enums.EventStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketEvent {
    private String evendID;
    private String questions;
    private String yesTicker;
    private String noTicker;
    private LocalDateTime expiry;
    private EventStatus status;
    private String outcome;
    private long liquidity;
    
    // Fix the field name typo
    public String getEventID() {
        return evendID;
    }
    
    public void setEventID(String eventID) {
        this.evendID = eventID;
    }
}