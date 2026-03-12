package org.example.matching.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.matching.api.dto.enums.EventStatus;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketEvent {
    private String eventID;
    private String questions;
    private String yesTicker;
    private String noTicker;
    private LocalDateTime expiry;
    private EventStatus status;
    private String outcome;
    private long liquidity;
    
    // This is the ONLY thing that drives price. Starts at 0.
    private final AtomicLong virtualNetSold = new AtomicLong(0);
    
    // Getter for the virtual counter
    public AtomicLong getVirtualNetSold() {
        return virtualNetSold;
    }
    
    // Fix the field name typo
    public String getEventID() {
        return eventID;
    }
    
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}