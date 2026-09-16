package ru.codeportfolio.tickethub.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.codeportfolio.tickethub.model.Event;

@Repository
public interface EventRepository extends ListCrudRepository<Event, Long> {
}
