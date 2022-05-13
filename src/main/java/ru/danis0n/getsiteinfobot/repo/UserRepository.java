package ru.danis0n.getsiteinfobot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danis0n.getsiteinfobot.model.entities.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findById(long id);
}
