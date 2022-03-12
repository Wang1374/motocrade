package com.laogeli.common.security.wx;

import com.laogeli.common.security.core.CustomUserDetailsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author yangyu
 * @date 2019-12-31
 */
@Slf4j
@Data
public class WxAuthenticationProvider implements AuthenticationProvider {

    private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WxAuthenticationToken wxAuthenticationToken = (WxAuthenticationToken) authentication;
        // 微信的code
        String principal = wxAuthenticationToken.getPrincipal().toString();
        UserDetails userDetails = customUserDetailsService.loadUserByWxCode(principal, wxAuthenticationToken.getWxUser());
        if (userDetails == null) {
            log.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.noopBindAccount", "Noop Bind Account"));
        }
        WxAuthenticationToken authenticationToken = new WxAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(wxAuthenticationToken.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
