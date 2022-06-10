package desktop.dating.controller;

import desktop.dating.model.Match;
import desktop.dating.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import desktop.dating.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/{id}/matches")
    public ResponseEntity<List<Match>> getUserMatches(@PathVariable long id) {
        return ResponseEntity.ok(
                userService.getUser(id).getMatches()
        );
    }
}
