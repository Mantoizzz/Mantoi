package com.forum.mantoi.sys.handler;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.common.Constants;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.exception.UserException;
import com.forum.mantoi.sys.model.JwtUser;
import com.forum.mantoi.sys.repository.UserRepository;
import com.forum.mantoi.utils.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JwtTokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final JwtUtilities jwtUtilities;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws UserException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UserException(CommonResultStatus.FAIL, "User not found"));
        JwtUser jwtUser = new JwtUser(user);
        String token = jwtUtilities.generateToken(user.getEmail(), jwtUser.getAuthorities());
        response.setHeader(Constants.JWT_HEADER, token);
    }

}
