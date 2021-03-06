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
public class DeletedMatch {
    @Id
    @SequenceGenerator(name = "deletedMatch_seq", sequenceName = "deletedMatch_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deletedMatch_seq")
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

    public DeletedMatch(Match match) {
        user1 = match.getUser1();
        user2 = match.getUser2();
        createdAt = match.getCreatedAt();
    }
}
