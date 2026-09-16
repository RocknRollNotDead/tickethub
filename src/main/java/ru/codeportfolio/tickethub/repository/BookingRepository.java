package ru.codeportfolio.tickethub.repository;

import org.springframework.data.repository.CrudRepository;
import ru.codeportfolio.tickethub.model.Booking;

public interface BookingRepository extends CrudRepository<Booking, Long> {
}
