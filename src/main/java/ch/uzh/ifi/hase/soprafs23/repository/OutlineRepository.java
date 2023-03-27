package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.Outline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("outlineRepository")
public interface OutlineRepository extends JpaRepository<Outline, Long>{
}
