package dev.newsletterss.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.newsletterss.api.entity.Member;
import dev.newsletterss.api.entity.Rss;
import dev.newsletterss.api.repository.MemberRepository;
import dev.newsletterss.api.service.FeedService;
import dev.newsletterss.api.service.MemberService;
import jdk.nashorn.internal.runtime.options.Option;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * RssFeed Controller
 * @author 이상일
 * @version 1.0
 * (2020.05.11) 이상일, 최초 작성
 */
@RestController
@AllArgsConstructor
@CrossOrigin
@Log
public class RssFeedController {
	@Autowired
	FeedService feedService;

	@Autowired
	MemberRepository rep;

	@GetMapping("/main/getTotalFeedsContents")
	public ResponseEntity<?> getTotalRssContents() throws Exception{
		String totalList = feedService.getTotalRssFeeds();
		HttpHeaders resHeaders = new HttpHeaders();
		resHeaders.set("Content-Type", "application/xml;charset=UTF-8");
		return  ResponseEntity.ok().headers(resHeaders).body(totalList);
	}
	@GetMapping("/main/rssFeedTest")
	public ResponseEntity<?> feedRequestTest(HttpServletRequest request) throws Exception {
		//카테고리
		String rssFeedNm = request.getParameter("rssFeedName");
		String entries = feedService.getFeedsBasedOnName(rssFeedNm);

		HttpHeaders resHeaders = new HttpHeaders();
		resHeaders.set("Content-Type", "application/xml;charset=UTF-8");

		return  ResponseEntity.ok().headers(resHeaders).body(entries);
	}
	@GetMapping("/main/rssFeedTest2")
	public ResponseEntity<?> feedRequestTest2(HttpServletRequest request) throws Exception {
		//카테고리
		String rssFeedNm = request.getParameter("testName");

		List<Member> test2 = rep.findAll();
		ObjectMapper obj = new ObjectMapper();
		String test = obj.writeValueAsString(test2);
		HttpHeaders resHeaders = new HttpHeaders();
		resHeaders.set("Content-Type", "application/xml;charset=UTF-8");

		return  ResponseEntity.ok().headers(resHeaders).body(test);
	}
	@PostMapping()
	public void SaveCustomizedFeed(HttpServletRequest request) throws Exception {

	}

}
