package dev.newsletterss.api.filter;

import dev.newsletterss.api.config.CustomTokenExceptionHandler;
import dev.newsletterss.api.config.JwtAuthenticationEntryPoint;
import dev.newsletterss.api.entity.TokenStorage;
import dev.newsletterss.api.repository.TokenStorageRepository;
import dev.newsletterss.api.service.JwtTokenUtilImpl;
import io.jsonwebtoken.JwtException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	@Autowired
	private TokenStorageRepository tokenRepository;

	private final AuthenticationManager authenticationManager;
	private final CustomTokenExceptionHandler customTokenExceptionHandler;

	public jwtAuthorizationFilter(AuthenticationManager authenticationManager,
								  ApplicationContext ctx,
								  CustomTokenExceptionHandler customTokenExceptionHandler) {
		super(authenticationManager);
		this.authenticationManager = authenticationManager;
		this.customTokenExceptionHandler = customTokenExceptionHandler;
		jwtTokenUtilImpl = ctx.getBean(JwtTokenUtilImpl.class);
		tokenRepository = ctx.getBean(TokenStorageRepository.class);
	}

	@SneakyThrows
	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		//request에서 가져온 Token값
		String reqAccToekn = request.getHeader("accessToken");
		String reqRefToken = request.getHeader("refreshToken");

		String usernamefromToken = null;
		String userrolefromToken = null;
		List<GrantedAuthority> authorities = new ArrayList<>();
		Authentication auth = null;

		//request Header에 token 값이 없는 경우 다음 filterChain으로 넘어간다
		if (StringUtils.isEmpty(reqAccToekn) || StringUtils.isEmpty(reqRefToken)) {
			chain.doFilter(request, response);
		}else {
			try{
				//accessToken parsing하기
				usernamefromToken = jwtTokenUtilImpl.getUsernameFromToken(reqAccToekn);
				userrolefromToken = jwtTokenUtilImpl.getUserroleFromToken(reqAccToekn);
				Boolean expTime = jwtTokenUtilImpl.isJwtTokenExpired(reqAccToekn);
				authorities.add(new SimpleGrantedAuthority(userrolefromToken));

				//accessToken의 만료기간이 지난 경우 db의 refreshToken 값과 비교하여 새로운 accessToken 발급
				if (expTime) {
					if (compareRequestTokenWithDb(request)) {// refreshToken 값 일치
						//accessToken 새로발급
						String newAccessToken = jwtTokenUtilImpl.createJwtToken(usernamefromToken, userrolefromToken);
						response.setHeader("accessToken", newAccessToken);
						response.setHeader("refreshToken", reqRefToken);
					} else { // 토큰 값이 일치하지 않는 경우(refreshToken 값이 손상 or 만료기간 지남)

					}
				}
				//token parsing한 값으로 authentication 객체 만들기
				auth = new UsernamePasswordAuthenticationToken(usernamefromToken, null, authorities);
				chain.doFilter(request, response);
				} catch (JwtException e) { // token 값으로 Authentication 객체를 생성하기 때문에 JwtExcption 선언
					customTokenExceptionHandler.onAuthenticationFailure(request, response, e);
				}
			}
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private boolean compareRequestTokenWithDb(HttpServletRequest request){
		boolean result;
		String reqRefToken = request.getHeader("refreshToken");
		Optional<TokenStorage> DbToken = tokenRepository.findByToken(reqRefToken);
		TokenStorage TokenStorageEntity = DbToken.get();
		String tokenValue = TokenStorageEntity.getToken();
		if(reqRefToken.equals(tokenValue)){
			result = true;
		}else {
			result = false;
		}
		return result;
	}

}
