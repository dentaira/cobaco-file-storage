package dentaira.cobaco.server.file.web;

import dentaira.cobaco.server.file.Owner;
import dentaira.cobaco.server.security.user.UserAccount;
import dentaira.cobaco.server.security.user.spring.AccountUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class OwnerArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Owner.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AccountUserDetails) {
            AccountUserDetails details = AccountUserDetails.class.cast(authentication.getPrincipal());
            UserAccount account = details.getAccount();
            return new Owner(account.getId());
        } else {
            return null;
        }
    }
}
