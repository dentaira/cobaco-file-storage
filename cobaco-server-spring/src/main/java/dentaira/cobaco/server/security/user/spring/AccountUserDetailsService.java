package dentaira.cobaco.server.security.user.spring;

import dentaira.cobaco.server.security.user.UserAccount;
import dentaira.cobaco.server.security.user.UserAccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AccountUserDetailsService implements UserDetailsService {

    private UserAccountRepository userAccountRepository;

    public AccountUserDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount account = userAccountRepository.findByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException(username + "のUserが見つかりませんでした。");
        }

        return new AccountUserDetails(account);
    }
}
