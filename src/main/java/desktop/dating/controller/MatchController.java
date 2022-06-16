package desktop.dating.controller;

import desktop.dating.dto.MatchDTO;
import desktop.dating.model.DeletedMatch;
import desktop.dating.model.Message;
import desktop.dating.repository.MessageRepository;
import desktop.dating.service.DeletedMatchService;
import desktop.dating.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import desktop.dating.service.MatchService;


@Controller
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final UserService userService;
    private final DeletedMatchService deletedMatchService;
    private final MessageRepository messageRepository;

    public MatchController(
            MatchService matchService,
            UserService userService,
            DeletedMatchService deletedMatchService,
            MessageRepository messageRepository
    ) {
        this.matchService = matchService;
        this.userService = userService;
        this.deletedMatchService = deletedMatchService;
        this.messageRepository = messageRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDTO> getMatch(@PathVariable int id, Authentication authentication) {
        var user = userService.getUser(authentication.getName());
        return ResponseEntity.ok(new MatchDTO(matchService.getMatch(id), user.getId()));
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<Void> sendMessage(@PathVariable int id, @RequestBody String content, Authentication authentication) {
        var match = matchService.getMatch(id);
        var sender = userService.getUser(authentication.getName());
        if (!match.getUser1().equals(sender) && !match.getUser2().equals(sender)) {
            return ResponseEntity.badRequest().build();
        }
        var message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(match.getUser1().equals(sender) ? match.getUser2() : match.getUser1());
        messageRepository.save(message);

        matchService.getMatch(id).addMessage(message);
        matchService.updateMatch(match);

        return ResponseEntity.ok().build();
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
