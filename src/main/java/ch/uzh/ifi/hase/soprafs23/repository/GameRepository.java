package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByGameId(Long id);
    List<Game> findAll();
    List<Game> findByOpenLobby(Boolean OpenLobbyStatus);
    @Query(value = "SELECT * FROM Game g WHERE (g.current_state = :guessingStateOrdinal OR g.current_state = :scoreboardStateOrdinal) AND g.last_update < DATEADD('SECOND', -1, CURRENT_TIMESTAMP)", nativeQuery = true)
    List<Game> findGamesToUpdate(@Param("guessingStateOrdinal") int guessingStateOrdinal, @Param("scoreboardStateOrdinal") int scoreboardStateOrdinal);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM Game g WHERE (g.current_state = :guessingStateOrdinal OR g.current_state = :scoreboardStateOrdinal))", nativeQuery = true)
    boolean areGamesStillOngoing(@Param("guessingStateOrdinal") int guessingStateOrdinal, @Param("scoreboardStateOrdinal") int scoreboardStateOrdinal);




}
