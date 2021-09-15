package dentaira.cobaco.server.authorization.security.config;

import dentaira.cobaco.server.authorization.security.user.UserAccountRepository;
import dentaira.cobaco.server.authorization.security.user.spring.AccountUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .formLogin()
                .and().build();
    }

    @Bean
    UserDetailsService userDetailsService(UserAccountRepository userRepo) {
        return email -> new AccountUserDetails(userRepo.findByEmail(email));
    }

}
