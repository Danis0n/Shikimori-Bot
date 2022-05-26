package ru.danis0n.getsiteinfobot.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.danis0n.getsiteinfobot.model.entities.UserState;
import ru.danis0n.getsiteinfobot.repo.StateRepository;

import java.util.List;

@Service
public class StateDAO {

    private final StateRepository stateRepository;

    @Autowired
    public StateDAO(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public UserState findByStateId(long id) {
        return stateRepository.findById(id);
    }

    public List<UserState> findAllStates() {
        return stateRepository.findAll();
    }

    public void removeState(UserState state) {
        stateRepository.delete(state);
    }

    public void save(UserState state) {
        stateRepository.save(state);
    }

    public boolean isExist(long id) {
        UserState state = findByStateId(id);
        return state != null;
    }


}
