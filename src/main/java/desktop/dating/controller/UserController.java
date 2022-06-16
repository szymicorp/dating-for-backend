package desktop.dating.controller;

import desktop.dating.dto.MatchDTO;
import desktop.dating.model.Like;
import desktop.dating.model.Match;
import desktop.dating.model.User;
import desktop.dating.service.LikeService;
import desktop.dating.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import desktop.dating.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final MatchService matchService;
    private final LikeService likeService;

    public UserController(UserService userService, MatchService matchService, LikeService likeService) {
        this.userService = userService;
        this.matchService = matchService;
        this.likeService = likeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        if (!user.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Password must be at least 8 characters long, contain upper and lower case letter and non standard character");
        }
        userService.addUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> addUsers(@RequestBody List<User> users) {
        for (var user : users) {
            addUser(user);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unseen")
    public ResponseEntity<List<User>> getUnseenProfiles(Authentication authentication) {
        var user = userService.getUser(authentication.getName());
        return ResponseEntity.ok(userService.getUnseenUsersForUser(user));
    }

    @GetMapping("/matches")
    public ResponseEntity<List<MatchDTO>> getUserMatches(Authentication authentication) {
        var user = userService.getUser(authentication.getName());
        return ResponseEntity.ok(
                user.getMatches().stream().map(match -> new MatchDTO(match, user.getId())).collect(Collectors.toList())
        );
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> giveLike(@PathVariable long id, Authentication authentication) {
        var sender = userService.getUser(authentication.getName());
        sender.getReceivedLikes()
                .stream()
                .filter(like -> like.getSender().getId() == id)
                .findFirst()
                .ifPresentOrElse(
                        like -> {
                            var match = new Match(like);
                            matchService.addMatch(match);
                            sender.addMatch(match);
                            userService.updateUser(sender);
                            var receiver = like.getSender();
                            receiver.addMatch(match);
                            userService.updateUser(receiver);
                            likeService.removeLike(like);
                        },
                        () -> {
                            var receiver = userService.getUser(id);
                            var like = new Like();
                            like.setReceiver(receiver);
                            like.setSender(sender);
                            like.setCreatedAt(LocalDateTime.now());
                            likeService.addLike(like);
                        }
                );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<Void> dislike(@PathVariable long id, Authentication authentication) {
        var disliker = userService.getUser(authentication.getName());
        var disliked = userService.getUser(id);
        disliker.addDisliked(disliked);
        userService.updateUser(disliker);
        return ResponseEntity.ok().build();
    }
}
