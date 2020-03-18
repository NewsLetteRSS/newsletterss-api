package dev.newsletterss.api.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dev.newsletterss.api.dto.MemberRequestDTO;
import dev.newsletterss.api.service.JwtTokenUtilImpl;
import dev.newsletterss.api.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
/**
 * Member Controller
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */
@RestController
@AllArgsConstructor
@CrossOrigin
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
    private JwtTokenUtilImpl jwtTokenUtilImpl;
	
	@PostMapping("/user/register")
	public ResponseEntity<?> saveUser(@RequestBody MemberRequestDTO memberRequestDto) throws Exception {
		return ResponseEntity.ok(memberService.joinMember(memberRequestDto));
	}

	@PostMapping("/user/login")
	public void loginMember(@RequestBody MemberRequestDTO memberRequestDto, HttpServletResponse response) throws Exception {

		//String accToken = jwtTokenUtilImpl.createJwtToken(memberRequestDto);
		//String refreshToken = jwtTokenUtilImpl.createJwtRefreshToken(memberRequestDto);
		//response.setHeader("accesstoken", accToken);
		//response.setHeader("refreshtoekn", refreshToken);
		
		System.out.println("ASDFASDFASDFASDFASDF");
	}
	
	@PostMapping("/auth/login")
	public ResponseEntity<?> authlogin(HttpServletResponse response) {
		System.out.println("authlogin");
		HashMap<String, Object> result = new HashMap<>();
		result.put("accessToken", response.getHeader("accessToken"));
		result.put("refreshToken", response.getHeader("refreshToken"));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/user/auth")
	public boolean verifyToken(HttpServletRequest request) throws Exception{
		String tokenString = request.getHeader("token");
		boolean chkToken = jwtTokenUtilImpl.verifyJwtToken(tokenString);

		return chkToken;
	}
}
