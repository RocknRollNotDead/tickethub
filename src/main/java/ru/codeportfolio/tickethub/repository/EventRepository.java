package ru.codeportfolio.tickethub.repository;

import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.codeportfolio.tickethub.model.Event;

import java.util.Optional;

@Repository
public interface EventRepository extends ListCrudRepository<Event, Long> {

    @Lock(LockMode.PESSIMISTIC_WRITE)
    Optional<Event> findWithLockById(@Param("id") Long id);
}
