package desktop.dating.dto;

import desktop.dating.model.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private long id;
    private String content;
    private String senderUsername;
    private String receiverUsername;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.senderUsername = message.getSender().getUsername();
        this.receiverUsername = message.getReceiver().getUsername();
    }
}
