package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.GameUser;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameUserRepository")
public interface GameUserRepository extends JpaRepository<GameUser, Long> {
}
