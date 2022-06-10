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
@Table(name = "\"LIKE\"")
public class Like {
    @Id
    @SequenceGenerator(name = "like_seq", sequenceName = "like_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_seq")
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User receiver;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;
}
