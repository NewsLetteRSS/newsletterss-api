package dev.newsletterss.api.service;

import dev.newsletterss.api.config.JwtTokenUtil;
import dev.newsletterss.api.dto.MemberRequestDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtTokenUtil interface를 구현하여 Token과 관련된 기능을 처리하는 클래스
 *
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */
@Component
public class JwtTokenUtilImpl implements JwtTokenUtil {

	private static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	/**
	 * AccessToken, RefreshToken 생성
	 *
	 * @ param String userid
	 * @ return Token String
	 * @ exception 예외사항
	 */
	@Override
	public String createJwtToken(String userid) throws Exception {

		Date accExpTime = Date.from(ZonedDateTime.now().toInstant().plus(Duration.ofSeconds(600)));
		Date accIatTime = Date.from(ZonedDateTime.now().toInstant());

		Map<String, Object> payloadMap = new HashMap<>();
		//private claim
		payloadMap.put("username", userid);

		String accessJws = Jwts.builder()
				.setHeaderParam("typ", "JWT")// alg 는 자동으로 생성
				.setIssuer("newsletterss")//토큰 발급자
				.setSubject("Access_token")//토큰 제목
				.setClaims(payloadMap)// private claim(사용자 이름과 role)
				.setIssuedAt(accIatTime)//토큰 발급시간
				.setExpiration(accExpTime)// 토큰 만료시간
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
		System.out.println(accessJws);
		return accessJws;
	}

    @Override
    public String createJwtRefreshToken(String userid) throws Exception {
        Date refreshExpTime = Date.from(ZonedDateTime.now().toInstant().plus(Duration.ofDays(30)));
        Date refreshIatTime = Date.from(ZonedDateTime.now().toInstant());

        Map<String, Object> payloadMap = new HashMap<>();
        //private claim
        payloadMap.put("username", userid);

        String refreshJws = Jwts.builder()
                .setHeaderParam("typ", "JWT")// alg 는 자동으로 생성
                .setIssuer("localhost")//토큰 발급자
                .setSubject("refresh_token")//토큰 제목
                .setClaims(payloadMap)// private claim(사용자 이름과 role)
                .setIssuedAt(refreshIatTime)//토큰 발급시간
                .setExpiration(refreshExpTime)// 토큰 만료시간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        System.out.println(refreshJws);
        return refreshJws;
    }

    /**
	 * 사용자가 보낸 토큰 검증
	 *
	 * @ param String jwtString 사용자가 보낸 token value
	 * @ return Token의 String 값
	 * @ exception 예외사항
	 */
	@Override
	public boolean verifyJwtToken(String jwtString) throws Exception {
		boolean chk = true;
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtString).getBody();
		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			chk = false;
		} catch (InvalidClaimException e) {
			e.printStackTrace();
			chk = false;
		}
		return chk;
	}
}
