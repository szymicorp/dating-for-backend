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
import java.util.ArrayList;
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
    @ElementCollection
    private List<String> photos = new ArrayList<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private boolean sex;
    private boolean premium;
    private String bio;
    @ElementCollection
    private List<String> interests = new ArrayList<>();

    private double maxDistance;
    private int minAge;
    private int maxAge;
    private boolean sexPreference;
    private boolean publicProfile;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    List<User> dislikes = new ArrayList<>();

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
        return birthDate.until(LocalDate.now()).getYears();
    }

    public void addDisliked(User user) {
        dislikes.add(user);
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

    public boolean wasntAlreadySeenBy(User user) {
        return receivedLikes.stream()
                .noneMatch(like ->
                        like.getSender().equals(user)
                ) &&
                matches.stream()
                .noneMatch(match ->
                        match.getUser1().equals(user) || match.getUser2().equals(user)
                );
    }
}
