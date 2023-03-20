package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Category;
import ch.uzh.ifi.hase.soprafs23.repository.CategoryRepository;
import ch.uzh.ifi.hase.soprafs23.repository.GameUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(@Qualifier("categoryRepository")CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

}
