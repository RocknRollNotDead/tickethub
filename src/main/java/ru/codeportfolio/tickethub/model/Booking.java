package ru.codeportfolio.tickethub.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("bookings")
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    private Long id;

    @Column("user_id")
    private Long ownerId;

    @Column("event_id")
    private Long eventId;

    private BookingStatus status;
}
