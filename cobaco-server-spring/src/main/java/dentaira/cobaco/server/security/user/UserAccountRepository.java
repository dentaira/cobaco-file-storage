package dentaira.cobaco.server.security.user;

public interface UserAccountRepository {

    public UserAccount findByEmail(String email);
}
