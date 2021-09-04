package dentaira.cobaco.server.security.user.web;

import dentaira.cobaco.server.security.user.UserAccount;

import java.util.UUID;

public class AccountResource {

    private UUID id;

    private String email;

    private String role;

    public AccountResource(UUID id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public static AccountResource of(UserAccount userAccount) {
        return new AccountResource(userAccount.getId(), userAccount.getEmail(), userAccount.getRole());
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
