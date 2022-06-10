package desktop.dating.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @SequenceGenerator(name = "match_seq", sequenceName = "match_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_seq")
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user1;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user2;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    public Match(Like like) {
        user1 = like.getSender();
        user2 = like.getReceiver();
        createdAt = LocalDateTime.now();
    }
}
