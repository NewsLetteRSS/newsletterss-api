package dev.newsletterss.api.dto;

import dev.newsletterss.api.entity.Member;
import dev.newsletterss.api.entity.TokenStorage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * memberRequest data DTO(Layer 간 데이터 교환을 위한 객체)
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */

@Getter
@NoArgsConstructor
public class TokenStorageDTO {

	private String username;
	private String userrole;
	private String token;

	public TokenStorage toEntity() {
		return TokenStorage.builder()
				.username(username)
				.userrole(userrole)
				.token(token)
				.build();
	}

	@Builder
	public TokenStorageDTO(String username, String userrole, String token) {
		this.username = username;
		this.userrole = userrole;
		this.token = token;
	}
}
