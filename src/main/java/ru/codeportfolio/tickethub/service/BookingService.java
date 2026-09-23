package ru.codeportfolio.tickethub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.codeportfolio.tickethub.model.Action;
import ru.codeportfolio.tickethub.model.Booking;
import ru.codeportfolio.tickethub.model.Event;
import ru.codeportfolio.tickethub.repository.BookingRepository;
import ru.codeportfolio.tickethub.repository.EventRepository;
import ru.codeportfolio.tickethub.repository.UserRepository;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TransactionTemplate transactionTemplate;
    private final ExecutorService notificationExecutor;
    private final AtomicLong bookingCounter;
    private final AuditService auditService;

    private volatile boolean maintenanceMode = false;

    @Retryable(includes = OptimisticLockingFailureException.class,
            maxRetries = 3, delay = 50)
    public void createBooking(Long eventId, Long userId) {
        transactionTemplate.execute(ignored -> {
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
            return null;
        });

        CompletableFuture.allOf(asyncSendToAnalytic(), asyncSendNotification())
                .thenRun( () -> {
                            bookingCounter.incrementAndGet();
                            auditService.incrementAction(Action.CREATE_BOOK);
                        }
                );
    }

    public void turnOnMaintenanceMode(){
        maintenanceMode = true;
    }

    private CompletableFuture<Void> asyncSendNotification() {
        return CompletableFuture.runAsync(() -> {
            try {
                sendNotification();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, notificationExecutor);
    }
    private CompletableFuture<Void> asyncSendToAnalytic() {
        return CompletableFuture.runAsync(() -> {
            try {
                writeToAnalytics();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, notificationExecutor);
    }



    private void sendNotification() throws InterruptedException {
        Thread.sleep(1000); // отправка уведомления
    }
    private void writeToAnalytics() throws InterruptedException {
        if (maintenanceMode) {
            Thread.sleep(1000); // запись во внешний сервис
        }
    }

}
