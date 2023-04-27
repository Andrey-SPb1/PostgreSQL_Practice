package jdbc.starter.dto;

import java.time.LocalDateTime;

public record FlightFilter (Long id,
        String FlightNO,
        LocalDateTime departureDate,
        String departureAirportCode,
        LocalDateTime arrivalDate,
        String arrivalAirportCode,
        Integer aircraftId,
        String status) {
}
