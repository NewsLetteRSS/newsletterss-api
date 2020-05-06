package dev.newsletterss.api.config;

import io.jsonwebtoken.JwtException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomTokenExceptionHandler{

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, JwtException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = "잘못된 접근입니다";
        json.put("code", "890");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
