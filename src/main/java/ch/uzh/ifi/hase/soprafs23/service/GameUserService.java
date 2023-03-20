package ch.uzh.ifi.hase.soprafs23.service;


import ch.uzh.ifi.hase.soprafs23.entity.GameUser;
import ch.uzh.ifi.hase.soprafs23.repository.GameUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameUserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final GameUserRepository gameUserRepository;

    @Autowired
    public GameUserService(@Qualifier("gameUserRepository") GameUserRepository gameUserRepository) {
        this.gameUserRepository = gameUserRepository;
    }


    public GameUser createGameUser(String username){
        GameUser gameUser = new GameUser();
        gameUser.setUsername(username);
        gameUserRepository.saveAndFlush(gameUser);
        return gameUser;
    }

}
