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
	private LocalDateTime reg_date;
	private LocalDateTime update_date;
	private String media;
	private String subname;

	public Rss toEntity() {
		return Rss.builder()
				.feedCode(feedCode)
				.feedCategory(feedCategory)
				.feedName(feedName)
				.feedType(feedType)
				.feedUrl(feedUrl)
				.reg_date(reg_date)
				.update_date(update_date)
				.media(media)
				.subname(subname)
				.build();
	}

	@Builder
	public FeedRequestDTO(String feedCode, String feedCategory, String feedName, String feedType, String feedUrl
			, LocalDateTime reg_date, LocalDateTime update_date, String media, String subname) {
		this.feedCode = feedCode;
		this.feedCategory = feedCategory;
		this.feedName = feedName;
		this.feedType = feedType;
		this.feedUrl = feedUrl;
		this.reg_date = reg_date;
		this.update_date = update_date;
		this.media = media;
		this.subname = subname;
	}
}
