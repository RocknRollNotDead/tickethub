package ru.codeportfolio.tickethub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Table("events")
@AllArgsConstructor
@Builder
public class Event {
    @Id
    private Long id;
    private String name;
    private Instant date;
    private Integer totalSeats;
    private Integer availableSeats;

    public void bookingSeat() {
        if (availableSeats <= 0) {
            throw new RuntimeException("Not seats!");
        }
        availableSeats--;
    }

    public void bookingSeat(Integer count) {
        int result = availableSeats - count;
        if (result <= 0) {
            throw new RuntimeException("Not available seats!");
        }
        availableSeats = result;
    }

}
