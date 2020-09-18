package dev.newsletterss.api.dto;

import dev.newsletterss.api.entity.Rss;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * feed data DTO(Layer 간 데이터 교환을 위한 객체)
 * @author 이상일
 * @version 1.0
 * (2020.05.19) 이상일, 최초 작성
 */

@Getter
@NoArgsConstructor
public class FeedRequestDTO {

	private String feedCode;
	private String feedCategory;
	private String feedName;
	private String feedType;
	private String feedUrl;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;

	public Rss toEntity() {
		return Rss.builder()
				.feedCode(feedCode)
				.feedCategory(feedCategory)
				.feedName(feedName)
				.feedType(feedType)
				.feedUrl(feedUrl)
				.regDate(regDate)
				.updateDate(updateDate)
				.build();
	}

	@Builder
	public FeedRequestDTO(String feedCode, String feedCategory, String feedName, String feedType, String feedUrl
			, LocalDateTime regDate, LocalDateTime updateDate) {
		this.feedCode = feedCode;
		this.feedCategory = feedCategory;
		this.feedName = feedName;
		this.feedType = feedType;
		this.feedUrl = feedUrl;
		this.regDate = regDate;
		this.updateDate = updateDate;
	}
}
