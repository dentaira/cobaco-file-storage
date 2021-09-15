package dentaira.cobaco.server.authorization.security.user;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserAccountRepository extends CrudRepository<UserAccount, UUID> {

    public UserAccount findByEmail(String email);
}
