package dentaira.cobaco.server.security.user;

import java.util.UUID;

public class UserAccount {

    private UUID id;

    private String email;

    private String name;

    private String password;

    private String role;

    public UserAccount(UUID id, String email, String name, String password, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
