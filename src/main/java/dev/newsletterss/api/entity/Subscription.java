package dev.newsletterss.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bytebuddy.agent.builder.AgentBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * subscription 테이블과 매칭되는 클래스
 * @author 이상일
 * @version 1.0
 * (2020.07.31) 이상일, 최초 작성
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rssCode")
    private String rssCode;
    @Column(name = "userId")
    private String  userId;
    @Column(name = "isDelete")
    private boolean isDelete;
    @Column(name = "customYn")
    private String customYn;
    @Column(name = "customText")
    private String customText;
    @Column(name = "regDate")
    @JsonIgnore
    private LocalDateTime regdate;
    @Column(name = "updateDate")
    private LocalDateTime updatedate;
    @Column(name = "code")
    private String code;
    @Column(name = "category")
    private String category;
    @Column(name = "type")
    private String type;
    @Column(name = "url")
    private String url;
    @Column(name = "name")
    private String name;

    @ManyToOne// subscription 입장에서는 member와 N:1 관계
    @JoinColumn(name ="userId",referencedColumnName = "userName", insertable = false, updatable = false)
    @JsonBackReference
    private Member member;

    @Builder
    public Subscription(Long id, String rssCode, String userId, boolean isDelete, String customYn
    , String customText, LocalDateTime regdate, LocalDateTime updatedate, String code, String category, String type, String url, String name ){
        this.id = id;
        this.rssCode = rssCode;
        this.userId = userId;
        this.isDelete = isDelete;
        this.customYn = customYn;
        this.customText = customText;
        this.regdate = regdate;
        this.updatedate = updatedate;
        this.code = code;
        this.category = category;
        this.type = type;
        this.url = url;
        this.name = name;
    }
}
