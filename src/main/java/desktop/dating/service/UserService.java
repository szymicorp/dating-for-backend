package desktop.dating.service;

import desktop.dating.model.DeletedMatch;
import desktop.dating.model.Match;
import desktop.dating.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import desktop.dating.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User getUser(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteMatch(User user, Match match) {
        user.removeMatch(match);
        updateUser(user);
    }

    public void addDeletedMatch(User user, DeletedMatch deletedMatch) {
        user.addDeletedMatch(deletedMatch);
        updateUser(user);
    }

    public List<User> getUnseenUsersForUser(User user) {
        return userRepository
                .findAll().stream().filter(fetchedUser ->
                        (fetchedUser.isSex() == user.isSexPreference()) &&
                        (fetchedUser.getAge() >= user.getMinAge()) &&
                        (fetchedUser.getAge() <= user.getMaxAge()) &&
                        !fetchedUser.equals(user) &&
                        fetchedUser.wasntAlreadySeenBy(user) &&
                        !user.getDislikes().contains(fetchedUser)
        ).limit(10).collect(Collectors.toList());
    }
}
