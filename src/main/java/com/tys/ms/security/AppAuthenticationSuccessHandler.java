package com.tys.ms.security;

import com.tys.ms.dao.UserAttemptsDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserAttemptsDaoImpl userAttemptsDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        userAttemptsDao.resetFailAttempts(httpServletRequest.getParameter("jobId"));
        httpServletResponse.sendRedirect("/info");
    }
}
