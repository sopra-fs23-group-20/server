package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameId(Long id);
    List<Game> findAll();
    List<Game> findByOpenLobby(Boolean OpenLobbyStatus);
    // Query to find games to update
    @Query(value = "SELECT * FROM game g WHERE (g.current_state = 1 OR g.current_state = 2) AND g.last_update < :oneSecondAgo", nativeQuery = true)
    List<Game> findGamesToUpdate( @Param("oneSecondAgo") Date oneSecondAgo);


    // Query to check if any games are still ongoing
    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM game g WHERE g.current_state = 1 OR g.current_state = 2) THEN TRUE ELSE FALSE END", nativeQuery = true)
    Boolean areGamesStillOngoing();


    @Query("SELECT g FROM Game g WHERE (g.currentState = 1 OR g.currentState = 2)")
    List<Game> findGamesToUpdateSimple(@Param("guessingStateOrdinal") int guessingStateOrdinal, @Param("scoreboardStateOrdinal") int scoreboardStateOrdinal);

}
