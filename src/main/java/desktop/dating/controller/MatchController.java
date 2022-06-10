package desktop.dating.controller;

import desktop.dating.model.DeletedMatch;
import desktop.dating.model.Match;
import desktop.dating.service.DeletedMatchService;
import desktop.dating.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import desktop.dating.service.MatchService;

import java.util.List;

@Controller
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final UserService userService;
    private final DeletedMatchService deletedMatchService;

    public MatchController(
            MatchService matchService,
            UserService userService,
            DeletedMatchService deletedMatchService
    ) {
        this.matchService = matchService;
        this.userService = userService;
        this.deletedMatchService = deletedMatchService;
    }

    @GetMapping
    public ResponseEntity<List<Match>> getMatches(@RequestParam("loggedId") long loggedId) {
        return ResponseEntity.ok(matchService.getMatchesForUser(loggedId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable int id) {
        var match = matchService.getMatch(id);
        var user1 = match.getUser1();
        var user2 = match.getUser2();
        userService.deleteMatch(user1, match);
        userService.deleteMatch(user2, match);
        matchService.removeMatch(match);
        var deletedMatch = new DeletedMatch(match);
        deletedMatchService.addDeletedMatch(deletedMatch);
        userService.addDeletedMatch(user1, deletedMatch);
        userService.addDeletedMatch(user2, deletedMatch);

        return ResponseEntity.ok().build();
    }
}
