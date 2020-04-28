package dev.newsletterss.api.filter;

import com.sun.istack.internal.NotNull;
import dev.newsletterss.api.config.CustomAccessDeniedHandler;
import dev.newsletterss.api.config.CustomAuthenticationFailureHandler;
import dev.newsletterss.api.config.JwtAuthenticationEntryPoint;
import dev.newsletterss.api.service.JwtTokenUtilImpl;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 인가 필터
 * @author 이상일
 * @version 1.0
 * (2020.04.05) 이상일, 최초 작성
 */
@Slf4j
public class jwtAuthorizationFilter extends BasicAuthenticationFilter {
	@Autowired
	private JwtTokenUtilImpl jwtTokenUtilImpl;

	private final AuthenticationManager authenticationManager;

	public jwtAuthorizationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
		super(authenticationManager);
		this.authenticationManager = authenticationManager;
		jwtTokenUtilImpl = ctx.getBean(JwtTokenUtilImpl.class);
	}

	@Override
	protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
		new JwtAuthenticationEntryPoint();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String reqAccToekn = request.getHeader("accessToken");
		String reqRefToekn = request.getHeader("refreshToken");
		//request Header에 token 값이 없는 경우 => 다음 filterChain으로 넘어간다
		if (StringUtils.isEmpty(reqAccToekn) || StringUtils.isEmpty(reqRefToekn)) {
			chain.doFilter(request, response);
		}else{
			if(jwtTokenUtilImpl.verifyToken(reqAccToekn)){ //토큰 유효성검사
				if(jwtTokenUtilImpl.isJwtTokenExpired(reqAccToekn)){ // 토큰 기간만료 검사
					chain.doFilter(request, response);
				}else{
					//DB의 RefreshToken과 Reqeust의 RefreshToken 비교


				}
			}else{
					//유효성 검사 false 일 때 Deniedhandler 를 호출해야 한다.()
			}
		}
	}
}
