package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.codeportfolio.tickethub.model.Booking;
import ru.codeportfolio.tickethub.model.Event;
import ru.codeportfolio.tickethub.repository.BookingRepository;
import ru.codeportfolio.tickethub.repository.EventRepository;
import ru.codeportfolio.tickethub.repository.UserRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public void createBooking(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Not found event!"));
        event.bookingSeat();
        eventRepository.save(event);
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not found user!"));
        bookingRepository.save(
                Booking.builder()
                        .ownerId(userId)
                        .eventId(eventId)
                        .build()
        );
    }
}
