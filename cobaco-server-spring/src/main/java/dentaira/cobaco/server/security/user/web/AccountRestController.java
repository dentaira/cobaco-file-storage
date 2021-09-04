package dentaira.cobaco.server.security.user.web;

import dentaira.cobaco.server.security.user.spring.AccountUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRestController {

    @PostMapping("api/sign-in/account")
    public AccountResource signIn(@AuthenticationPrincipal AccountUserDetails account) {
        return AccountResource.of(account.getAccount());
    }

}