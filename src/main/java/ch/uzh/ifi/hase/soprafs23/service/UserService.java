package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entityDB.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(String authHeader) {
        User userByToken = userRepository.findByToken(authHeader);
        if (userByToken == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized");
        userByToken.setStatus(UserStatus.ONLINE);
        userRepository.flush();
        return this.userRepository.findAll();
    }


    public User getUserById(Long id, String authHeader) {
        User userByID = userRepository.findByUserId(id);
        User userByToken = userRepository.findByToken(authHeader);
        if (userByID == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " was not found");
        }
        if (userByID != userByToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized");
        }
        userByToken.setStatus(UserStatus.ONLINE);
        userRepository.flush();
        return userByID;

    }

    public User getUserByIdGeneralAuth(Long id, String authHeader) {
        User userByID = userRepository.findByUserId(id);
        User userByToken = userRepository.findByToken(authHeader);
        if (userByID == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " was not found");
        }
        if (userByToken == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized");
        userByToken.setStatus(UserStatus.ONLINE);
        userRepository.flush();
        return userByID;
    }


    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        String baseErrorMessage = "add User failed because user with username: %s already exists";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, userToBeCreated.getUsername()));
        }
    }

    public User loginUser(User user) {
        try {
            if (user.getUsername().isEmpty() || user.getPassword().isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is empty");
        }
        catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is empty");
        }
        User userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username: " + user.getUsername() + " was not found");
        }
        if (userByUsername.getPassword().equals(user.getPassword())) {
            userByUsername.setStatus(UserStatus.ONLINE);
            userByUsername.setToken(UUID.randomUUID().toString());
            userRepository.flush();
            return userByUsername;
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
        }
    }

    public User createUser(User newUser) {
        try {
            if (newUser.getUsername().isEmpty() || newUser.getPassword().isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is empty");
        }
        catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is empty, null pointer exception");
        }
        if (newUser.getUsername().contains(" ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot contain spaces");
        }

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreation_date(new Date());
        checkIfUserExists(newUser);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }


    public boolean updateUser(Long userId, String authHeader, User userInput) {
        User originalUser = getUserById(userId, authHeader);
        if (originalUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " was not found");
        }

        if (!originalUser.getToken().equals(authHeader))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized");

        if (userRepository.findByUsername(userInput.getUsername()) != null && !userInput.getUsername().equals(originalUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Updating user failed because user with username: " + userInput.getUsername() + " already exists");
        }


        if (userInput.getUsername() != null) {
            if (userInput.getUsername().contains(" ")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot contain spaces");
            }
            originalUser.setUsername(userInput.getUsername());
        }


        originalUser.setBirthday(userInput.getBirthday());


        if (userInput.getStatus() != null) {
            originalUser.setStatus(userInput.getStatus());
        }
        userRepository.flush();
        return true;
    }
}
