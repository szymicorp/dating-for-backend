package desktop.dating.controller;

import desktop.dating.model.Like;
import desktop.dating.model.Match;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import desktop.dating.service.LikeService;
import desktop.dating.service.MatchService;
import desktop.dating.service.UserService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;
    private final MatchService matchService;

    public LikeController(LikeService likeService, UserService userService, MatchService matchService) {
        this.likeService = likeService;
        this.userService = userService;
        this.matchService = matchService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> giveLike(@PathVariable long userId, @RequestParam("loggedId") long loggedId) {
        var sender = userService.getUser(loggedId);
        sender.getReceivedLikes()
              .stream()
              .filter(like -> like.getSender().getId() == userId)
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
                          var receiver = userService.getUser(userId);
                          var like = new Like();
                          like.setReceiver(receiver);
                          like.setSender(sender);
                          like.setCreatedAt(LocalDateTime.now());
                          likeService.addLike(like);
                      }
              );
        return ResponseEntity.ok().build();
    }
}
