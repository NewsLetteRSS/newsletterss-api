package dev.newsletterss.api.config;

import dev.newsletterss.api.filter.jwtAuthenticationFilter;
import dev.newsletterss.api.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MemberService memberService;

	@Bean
	public AccessDeniedHandler customAccessDeniedHandler(){
		return new CustomAccessDeniedHandler();
	}
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	/*
	 * bcypt 알고리즘을 이용하여 데이터를 암호화한다
	 * @ return PasswordEncoder BCryptPasswordEncoder
	 */
	@Autowired
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		// 아래 경로는 어떤 사용자건 접근이 가능하다
		.authorizeRequests().antMatchers("/main", "/user").permitAll()
		// 아래의 경로는 인증을 받아야 접근 가능하다.
		.antMatchers("/auth/login", "/newsletterssAPI/auth/**", "/user/**").authenticated()
		.and()
		.addFilter(new jwtAuthenticationFilter(authenticationManager(), getApplicationContext()))
		//인증 관련 에러는 AuthenticationEntryPoint로, 인가 관련 에러는 customAccessDeniedHandler로 보내겠다
		.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler()).authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
		//세션을 사용하지 않겠다
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	// 사용자의 유저네임과 패스워드가 맞는지 검증
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}
}
