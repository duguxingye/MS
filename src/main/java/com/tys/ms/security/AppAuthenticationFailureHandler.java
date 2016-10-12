package com.tys.ms.security;

import com.tys.ms.dao.UserAttemptsDaoImpl;
import com.tys.ms.model.User;
import com.tys.ms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final static int MAX_ATTEMPTS = 21;

    @Autowired
    UserService userService;

    @Autowired
    UserAttemptsDaoImpl userAttemptsDao;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String userName = httpServletRequest.getParameter("jobId");
        userAttemptsDao.updateFailAttempts(userName);
        if (userAttemptsDao.getUserAttempts(userName).getAttempts() + 1 >= MAX_ATTEMPTS) {
            User user = userService.findByJobId(userName);
            user.setHasLocked(true);
            userService.updateUser(user);
        }
        httpServletResponse.sendRedirect("/login?error");
    }
}
