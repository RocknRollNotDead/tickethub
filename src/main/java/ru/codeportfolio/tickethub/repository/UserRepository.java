package ru.codeportfolio.tickethub.repository;

import org.springframework.data.repository.CrudRepository;
import ru.codeportfolio.tickethub.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
