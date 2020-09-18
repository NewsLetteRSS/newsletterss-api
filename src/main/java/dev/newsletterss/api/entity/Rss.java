package dev.newsletterss.api.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * rss 테이블과 매칭되는 클래스
 * @author 이상일
 * @version 1.0
 * (2020.05.19) 이상일, 최초 작성
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="rss")
public class Rss {

    @Id
    @Column(name = "code",length = 100)
    private String feedCode;
    @Column(name = "category")
    private String feedCategory;
    @Column(name = "name")
    private String feedName;
    @Column(name = "type")
    private String feedType;
    @Column(name = "url")
    private String feedUrl;
    @Column(name = "regDate")
    private LocalDateTime regDate;
    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Builder
    public Rss(String feedCode, String feedCategory, String feedName, String feedType, String feedUrl
    , LocalDateTime regDate, LocalDateTime updateDate ){
        this.feedCode = feedCode;
        this.feedCategory = feedCategory;
        this.feedName = feedName;
        this.feedType = feedType;
        this.feedUrl = feedUrl;
        this.regDate = regDate;
        this.updateDate = updateDate;
    }
}
