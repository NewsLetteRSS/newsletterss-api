package dev.newsletterss.api.service;

import dev.newsletterss.api.config.JwtTokenUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
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
@Log
public class JwtTokenUtilImpl implements JwtTokenUtil {

	private static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	/**
	 * AccessToken, RefreshToken 생성
	 *
	 * @ param String userid
	 * @ param String userrole
	 * @ return Token String
	 * @ exception 예외사항
	 */
	@Override
	public String createJwtToken(String userid, String userrole) throws Exception {

		Date accExpTime = Date.from(ZonedDateTime.now().toInstant().plus(Duration.ofSeconds(600)));
		Date accIatTime = Date.from(ZonedDateTime.now().toInstant());

		Map<String, Object> payloadMap = new HashMap<>();
		//private claim
		payloadMap.put("username", userid);
		payloadMap.put("userrole", userrole);

		String accessJws = Jwts.builder()
				.setHeaderParam("typ", "JWT")// alg 는 자동으로 생성
				.setIssuer("newsletterss")//토큰 발급자
				.setSubject("Access_token")//토큰 제목
				.setClaims(payloadMap)// private claim(사용자 이름과 role)
				.setIssuedAt(accIatTime)//토큰 발급시간
				.setExpiration(accExpTime)// 토큰 만료시간
				.signWith(secretKey, SignatureAlgorithm.HS256)
				.compact();
		return accessJws;
	}

    @Override
    public String createJwtRefreshToken(String userid, String userrole) throws Exception {
        Date refreshExpTime = Date.from(ZonedDateTime.now().toInstant().plus(Duration.ofDays(30)));
        Date refreshIatTime = Date.from(ZonedDateTime.now().toInstant());

        Map<String, Object> payloadMap = new HashMap<>();
        //private claim
        payloadMap.put("username", userid);
		payloadMap.put("userrole", userrole);

        String refreshJws = Jwts.builder()
                .setHeaderParam("typ", "JWT")// alg 는 자동으로 생성
                .setIssuer("newsletterss")//토큰 발급자
                .setSubject("refresh_token")//토큰 제목
                .setClaims(payloadMap)// private claim(사용자 이름과 role)
                .setIssuedAt(refreshIatTime)//토큰 발급시간
                .setExpiration(refreshExpTime)// 토큰 만료시간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        return refreshJws;
    }

    /**
	 * user가 보낸 token의 유효성 검사
	 *
	 * @ param String jwtString 사용자가 보낸 token value
	 * @ return 검증값
	 * @ exception 예외사항
	 * @return
	 */
    public boolean verifyToken(String jwtString){
    	boolean verificationToken;
    	try {
    		Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtString).getBody().getExpiration();
			verificationToken = true;
		}catch (MalformedJwtException e){ // 잘못된 claim 구조
			log.info(e.getMessage());
			verificationToken = false;
		}catch (IncorrectClaimException e){ // 잘못된 claim value
			log.info(e.getMessage());
			verificationToken = false;
		}catch (SignatureException e){ //서명 검증 error
			log.info(e.getMessage());
			verificationToken = false;
		}catch(UnsupportedJwtException e){// 형식이 맞지않는 토큰
			log.info(e.getMessage());
			verificationToken = false;
		}
    	return verificationToken;
	}
	/**
	 * user가 보낸 token의 유효기간 검사
	 *
	 * @ param String jwtString 사용자가 보낸 token value
	 * @ return 검증값
	 * @ exception 예외사항
	 * @return
	 */
	public boolean isJwtTokenExpired(String jwtString){
		boolean tokenExp;
		try{
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtString).getBody().getExpiration();
			tokenExp = true;
		} catch(ExpiredJwtException e){
			log.info(e.getMessage());
			tokenExp = false;
		}
		return tokenExp;
	}
	@Override
	public Claims getClaimsFromJwtToken(String jwtString) throws Exception{
		return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtString).getBody();
	}
}
