package dentaira.cobaco.server.security.user.infra;

import dentaira.cobaco.server.security.user.UserAccount;
import dentaira.cobaco.server.security.user.UserAccountRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JdbcUserAccountRepository implements UserAccountRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserAccount findByEmail(String email) {
        return jdbcTemplate.query(
                "SELECT id, email, name, password, role FROM user_account WHERE email = ?",
                pss -> pss.setString(1, email),
                rs -> {
                    if (rs.next()) {
                        return new UserAccount(
                                UUID.fromString(rs.getString("id")),
                                rs.getString("email"),
                                rs.getString("name"),
                                rs.getString("password"),
                                rs.getString("role")
                        );
                    } else {
                        return null;
                    }
                }
        );
    }
}
