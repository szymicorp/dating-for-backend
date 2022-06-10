package desktop.dating.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
// H2 database uses USER / User as a keyword,
// therefore it needs to be escaped properly
@Table(name = "\"USER\"")
public class User {
    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private long id;
    @Column(unique = true)
    private String username;
    @Pattern(regexp = "[a-z]+")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String firstName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private boolean sex;
    private boolean premium;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    List<User> dislikes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "receiver")
    @JsonIgnore
    List<Like> receivedLikes;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    List<Match> matches;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    List<DeletedMatch> deletedMatches;

    public int getAge() {
        return 18;
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void removeMatch(Match match) {
        matches.remove(match);
    }

    public void addDeletedMatch(DeletedMatch deletedMatch) {
        deletedMatches.add(deletedMatch);
    }
}
