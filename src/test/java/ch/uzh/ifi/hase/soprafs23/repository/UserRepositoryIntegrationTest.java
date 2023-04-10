package ch.uzh.ifi.hase.soprafs23.repository;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_success() {
        // given
        User user = new User();
        user.setPassword("testPswd");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setCreation_date(new Date());
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertNotNull(found.getUserId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
    }

    @Test
    public void findByUserId_success() {
        // given
        User user = new User();
        user.setPassword("testPswd");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setCreation_date(new Date());
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUserId(user.getUserId());

        // then
        assertNotNull(found);
        assertEquals(found.getUserId(), user.getUserId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
    }

    @Test
    public void findByToken_success() {
        // given
        User user = new User();
        user.setPassword("testPswd");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setCreation_date(new Date());
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByToken(user.getToken());

        // then
        assertNotNull(found);
        assertEquals(found.getUserId(), user.getUserId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
    }

    @Test
    public void findByUsername_notFound() {
        // when
        User found = userRepository.findByUsername("nonExistentUsername");

        // then
        assertNull(found);
    }

    @Test
    public void findByUserId_notFound() {
        // when
        User found = userRepository.findByUserId(-1L);

        // then
        assertNull(found);
    }

    @Test
    public void findByToken_notFound() {
        // when
        User found = userRepository.findByToken("nonExistentToken");

        // then
        assertNull(found);
    }

    @Test
    public void saveUser_success() {
        // given
        User user = new User();
        user.setPassword("testPswd");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setCreation_date(new Date());
        user.setToken("1");

        // when
        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertNotNull(found);
        assertEquals(found.getUserId(), user.getUserId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
    }

    @Test
    public void updateUser_success() {
        // given
        User user = new User();
        user.setPassword("testPswd");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setCreation_date(new Date());
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
        User updatedUser = userRepository.findByUserId(user.getUserId());

        // then
        assertEquals(updatedUser.getStatus(), UserStatus.ONLINE);
    }

    @Test
    public void deleteUser_success() {
        // given
        User user = new User();
        user.setPassword("testPswd");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setCreation_date(new Date());
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        userRepository.delete(user);
        User deletedUser = userRepository.findByUserId(user.getUserId());

        // then
        assertNull(deletedUser);
    }

    @Test
    public void countUsers_success() {
        // given
        User user1 = new User();
        user1.setPassword("testPswd");
        user1.setUsername("firstname@lastname");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setCreation_date(new Date());
        user1.setToken("1");

        User user2 = new User();
        user2.setPassword("testPswd");
        user2.setUsername("anotherfirstname@anotherlastname");
        user2.setStatus(UserStatus.OFFLINE);
        user2.setCreation_date(new Date());
        user2.setToken("2");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // when
        Long count = userRepository.count();

        // then
        assertEquals(count, 2L);
    }


}
