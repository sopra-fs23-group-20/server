package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.entity.Category;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("categoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
