package dev.newsletterss.api.dto;

import java.time.LocalDateTime;
import dev.newsletterss.api.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * memberRequest data DTO(Layer 간 데이터 교환을 위한 객체)
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */

@Getter
@NoArgsConstructor
public class MemberRequestDTO {

	private String username;
	private String password;
	private String email;
	private LocalDateTime regdate;
	private String userrole;
	
	public Member toEntity() {
		return Member.builder()

				.username(username)
				.password(password)
				.email(email)
				.regdate(regdate)
				.userrole(userrole)
				.build();
	}
	
	@Builder
	public MemberRequestDTO( String username, String password, String email, LocalDateTime regdate, String userrole) {
		this.username=username;
		this.password = password;
		this.email = email;
		this.regdate = regdate;
		this.userrole = userrole;
	}
}
