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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AllArgsConstructor;
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
		// dont authenticate this particular request
		.authorizeRequests().antMatchers("/user/auth",
				"/user/register").permitAll()
		//anyRequest().permitAll().and()
		// all other requests need to be authenticated
		.antMatchers("/auth/loginSuccess", "/newsletterssAPI/**").authenticated()
		.and()
		.addFilter(new jwtAuthenticationFilter(authenticationManager(), getApplicationContext()));
		//.and()
		// make sure we use stateless session; session won't be used to
		// store user's state.
		//.exceptionHandling().authenticationEntryPoint().and().sessionManagement()
		//.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
	// 사용자의 유저네임과 패스워드가 맞는지 검증
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}
}
