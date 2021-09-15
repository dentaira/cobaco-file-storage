package dentaira.cobaco.server.authorization.security.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PUBLIC, force=true)
public class UserAccount {

    @Id
    private UUID id;

    private String email;

    private String name;

    private String password;

    private String role;

}
