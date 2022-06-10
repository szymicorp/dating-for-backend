package desktop.dating.dto;

import desktop.dating.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private boolean sex;
    private boolean premium;

    public UserDTO(User user) {
        id = user.getId();
        username = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        age = user.getAge();
        sex = user.isSex();
        premium = user.isPremium();
    }
}
