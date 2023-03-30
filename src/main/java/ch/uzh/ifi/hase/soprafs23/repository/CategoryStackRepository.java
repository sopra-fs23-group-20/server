package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entityDB.CategoryStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("categoryStackRepository")
public interface CategoryStackRepository extends JpaRepository<CategoryStack, Long> {

}
