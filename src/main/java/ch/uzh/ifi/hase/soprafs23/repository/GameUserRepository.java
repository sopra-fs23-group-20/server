package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import ch.uzh.ifi.hase.soprafs23.entityDB.GameUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gameUserRepository")
public interface GameUserRepository extends JpaRepository<GameUser, Long>{


}
