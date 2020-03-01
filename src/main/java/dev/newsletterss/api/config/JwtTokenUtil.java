package dev.newsletterss.api.config;

import dev.newsletterss.api.dto.MemberRequestDTO;

/**
 * JwtTokenUtil interface
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */
public interface JwtTokenUtil {
	public String createJwtToken(String userid) throws Exception;
	public String createJwtRefreshToken(String userid) throws Exception;
	public boolean verifyJwtToken(String jwtString) throws Exception;
}
