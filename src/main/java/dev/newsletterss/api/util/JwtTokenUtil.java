package dev.newsletterss.api.util;

import dev.newsletterss.api.dto.MemberRequestDTO;
import io.jsonwebtoken.Claims;

/**
 * JwtTokenUtil interface
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */
public interface JwtTokenUtil {
	public String createJwtToken(String userid, String userrole) throws Exception;
	public String createJwtRefreshToken(String userid, String userrole) throws Exception;
	public Claims getClaimsFromJwtToken(String jwtString) throws Exception;
}
