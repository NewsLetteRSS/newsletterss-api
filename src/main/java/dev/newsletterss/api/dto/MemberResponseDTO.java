package dev.newsletterss.api.dto;

import dev.newsletterss.api.entity.Member;
import lombok.Builder;
import lombok.Getter;

/**
 * memberResponse data DTO(Layer 간 데이터 교환을 위한 객체)
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */


@Getter
public class MemberResponseDTO {
	
	private final String jwtToken;
	private String username;
	private String userrole;

	public Member toEntity() {
		return Member.builder()
				.username(username)
				.userrole(userrole)
				.build();
	}
	
	@Builder
	public MemberResponseDTO(String jwtToken, String username, String userrole) {
		this.jwtToken = jwtToken;
		this.username = username;
		this.userrole = userrole;
	}
}
