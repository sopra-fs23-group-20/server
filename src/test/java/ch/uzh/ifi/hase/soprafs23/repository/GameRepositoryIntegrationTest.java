package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void findByGameId_success() {
        // given
        Game game = new Game();
        game.setGameId(1L);

        entityManager.persist(game);
        entityManager.flush();

        // when
        Game found = gameRepository.findByGameId(game.getGameId());

        // then
        assertNotNull(found);
        assertEquals(found.getGameId(), game.getGameId());
    }

    @Test
    public void findAll_success() {
        // given
        Game game1 = new Game();
        game1.setGameId(1L);

        Game game2 = new Game();
        game2.setGameId(2L);

        entityManager.persist(game1);
        entityManager.persist(game2);
        entityManager.flush();

        // when
        List<Game> found = gameRepository.findAll();

        // then
        assertNotNull(found);
        assertEquals(found.size(), 2);
    }

    @Test
    public void findByOpenLobby_success() {
        // given
        Game game1 = new Game();
        game1.setGameId(1L);
        game1.setOpenLobby(true);

        Game game2 = new Game();
        game2.setGameId(2L);
        game2.setOpenLobby(false);

        entityManager.persist(game1);
        entityManager.persist(game2);
        entityManager.flush();

        // when
        List<Game> found = gameRepository.findByOpenLobby(true);

        // then
        assertNotNull(found);
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getGameId(), game1.getGameId());
    }

    @Test
    public void findByGameId_notFound() {
        // when
        Game found = gameRepository.findByGameId(-1L);

        // then
        assertNull(found);
    }

    @Test
    public void save_success() {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setOpenLobby(true);

        // when
        Game saved = gameRepository.save(game);
        entityManager.flush();

        // then
        assertNotNull(saved.getGameId());
        assertEquals(saved.getGameId(), game.getGameId());
    }

    @Test
    public void delete_success() {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setOpenLobby(true);

        entityManager.persist(game);
        entityManager.flush();

        // when
        gameRepository.delete(game);
        entityManager.flush();

        // then
        Game deleted = entityManager.find(Game.class, game.getGameId());
        assertNull(deleted);
    }

    @Test
    public void deleteById_success() {
        // given
        Game game = new Game();
        game.setGameId(1L);
        game.setOpenLobby(true);

        entityManager.persist(game);
        entityManager.flush();

        // when
        gameRepository.deleteById(game.getGameId());
        entityManager.flush();

        // then
        Game deleted = entityManager.find(Game.class, game.getGameId());
        assertNull(deleted);
    }

    @Test
    public void findByOpenLobby_notFound() {
        // given
        Game game1 = new Game();
        game1.setGameId(1L);
        game1.setOpenLobby(false);

        Game game2 = new Game();
        game2.setGameId(2L);
        game2.setOpenLobby(false);

        entityManager.persist(game1);
        entityManager.persist(game2);
        entityManager.flush();

        // when
        List<Game> found = gameRepository.findByOpenLobby(true);

        // then
        assertNotNull(found);
        assertEquals(found.size(), 0);
    }

    @Test
    public void findAll_empty() {
        // when
        List<Game> found = gameRepository.findAll();

        // then
        assertNotNull(found);
        assertEquals(found.size(), 0);
    }



}
