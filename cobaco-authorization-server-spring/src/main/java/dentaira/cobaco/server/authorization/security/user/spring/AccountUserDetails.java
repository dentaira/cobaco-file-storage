package dentaira.cobaco.server.authorization.security.user.spring;

import dentaira.cobaco.server.authorization.security.user.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AccountUserDetails implements UserDetails {

    private UserAccount account;

    private Collection<GrantedAuthority> authorities;

    public AccountUserDetails(UserAccount account) {
        this.account = account;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UUID getId() {
        return this.account.getId();
    }

    public UserAccount getAccount() {
        return this.account;
    }
}
