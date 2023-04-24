package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.GameState;
import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameId(Long id);
    List<Game> findAll();
    List<Game> findByOpenLobby(Boolean OpenLobbyStatus);
    List<Game> findByCurrentStateIs(GameState GameState);
}
