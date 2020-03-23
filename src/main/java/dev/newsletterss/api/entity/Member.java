package dev.newsletterss.api.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * Member 테이블과 매칭되는 클래스
 * @author 이상일
 * @version 1.0
 * (2020.01.29) 이상일, 최초 작성
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="member")
public class Member {

	@Id
	@Column(name = "userName",length = 100)
	private String username;

	/*@Column(name = "username")
	private String username;*/
	@Column(name = "password")
	private String password;
	@Column(name = "email")
	private String email;
	@Column(name = "regDate")
	private LocalDateTime regdate;
	@Column(name = "userRole")
	private String userrole;
	
	@Builder
	public Member( String username, String password, String email, LocalDateTime regdate, String userrole) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.regdate = regdate;
		this.userrole = userrole;
	}
}
