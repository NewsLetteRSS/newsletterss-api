package dev.newsletterss.api.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;
import dev.newsletterss.api.dto.MemberRequestDTO;
import dev.newsletterss.api.service.MemberService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * (2020.04.28) 이상일, authlogin 수정
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@Log
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@PostMapping("/user")
	public ResponseEntity<?> saveUser(@RequestBody MemberRequestDTO memberRequestDto) throws Exception {
		return ResponseEntity.ok(memberService.joinMember(memberRequestDto));
	}

	@PostMapping("/auth/login")
	public ResponseEntity<?> authlogin(HttpServletResponse response) throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		result.put("accessToken", response.getHeader("accessToken"));
		result.put("refreshToken", response.getHeader("refreshToken"));
		memberService.saveRefreshTokenInMaria(response.getHeader("refreshToken"));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	@PostMapping("/auth/authorization")
	public ResponseEntity<?>  authorizationRequestTest() throws Exception {
		log.info("----------------------인가 성공-----------------------");
		return new ResponseEntity<>("토큰 검증 통과", HttpStatus.OK);
	}
}
