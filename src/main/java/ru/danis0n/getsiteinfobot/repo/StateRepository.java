package ru.danis0n.getsiteinfobot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danis0n.getsiteinfobot.model.entities.UserState;

public interface StateRepository extends JpaRepository<UserState,Long> {
    UserState findById(long id);
    UserState deleteById(long id);
}
