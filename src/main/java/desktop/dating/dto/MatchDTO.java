package desktop.dating.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import desktop.dating.model.Match;
import desktop.dating.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MatchDTO {
    private long id;
    private User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
    private List<MessageDTO> messages;

    public MatchDTO(Match match, long requesterId) {
        this.id = match.getId();
        if (match.getUser1().getId() == requesterId) {
            this.user = match.getUser2();
        } else {
            this.user = match.getUser1();
        }
        this.createdAt = match.getCreatedAt();
        this.messages = match.getMessages().stream().map(MessageDTO::new).collect(Collectors.toList());
    }
}
