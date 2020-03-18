package dev.newsletterss.api.config;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("User: " + auth.getName()
                    + " attempted to access the protected URL: "
                    + request.getRequestURI());
        }else{
            System.out.print("인증객체없다");
        }
        accessDeniedException.getCause();
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "잘못된 접근입니다");
    }
}
