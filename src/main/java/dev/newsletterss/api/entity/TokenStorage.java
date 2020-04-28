package dev.newsletterss.api.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TokenStorage 테이블과 매칭되는 클래스
 * @author 이상일
 * @version 1.0
 * (2020.04.28) 이상일, 최초 작성
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="tokenstorage")
public class TokenStorage {
    @Id
    @Column(name = "userName",length = 100)
    private String username;
    @Column(name = "userRole")
    private String userrole;
    @Column(name = "token")
    private String tokenvalue;

    @Builder
    public TokenStorage(String username, String userrole, String tokenvalue) {
        this.username = username;
        this.userrole = userrole;
        this.tokenvalue = tokenvalue;
    }

}
