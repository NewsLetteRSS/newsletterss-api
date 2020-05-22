package dev.newsletterss.api.service;
/**
 * 카테고리, 언론사 별 feed 반환
 * @author 이상일
 * @version 1.0
 * (2020.05.19) 이상일, 최초 작성
 */

import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import dev.newsletterss.api.entity.Rss;
import dev.newsletterss.api.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.jdom.Element;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.JSONArray;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log
public class FeedService   {

	private final FeedRepository feedRepository;

	/*
	 * 카테고리 기준 rss
	 *
	 * @ param String reqCategory, String reqMedia 사용자가 선택한 카테고리
	 * @ return List contents
	 */
	public String getFeedsBasedOnCategory(String reqMedia, String reqCategory) throws Exception{
		Optional<Rss> feedWrapper = feedRepository.findByMediaAndSubname(reqMedia, reqCategory);
		//List contents = getEntriesFromFeeds(feedWrapper);
		String contents = getEntriesFromFeeds(feedWrapper);
		return contents;
	}
	/*
	 * rss 정보를 바탕으로 feed 생성
	 *
	 * @ param Optional<Rss> feedWrapper db에서 사용자의 카테고리 기준으로 가져온 rss 정보
	 * @ return List getEntries
	 */
	public String getEntriesFromFeeds(Optional<Rss> feedWrapper) throws Exception{
		Rss feeds = feedWrapper.get();
		URL feedSource = new URL(feeds.getFeedUrl());
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedSource));

		ArrayList list = new ArrayList();

		List entry = feed.getEntries();
		list.addAll(entry);
		SyndFeedOutput output = new SyndFeedOutput();
		output.output(feed, new PrintWriter(System.out));

		JSONObject json = XML.toJSONObject(output.outputString(feed));
		String jsonString = json.toString();

		return jsonString;
	}


}