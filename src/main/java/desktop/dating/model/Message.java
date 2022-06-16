package desktop.dating.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    private long id;
    private String content;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;
}
