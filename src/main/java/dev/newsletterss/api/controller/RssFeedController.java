package dev.newsletterss.api.controller;

import dev.newsletterss.api.service.FeedService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
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
	@GetMapping("/auth/rss/rssFeedTest")
	@ResponseBody
	public ResponseEntity<?> feedRequestTest(HttpServletRequest request) throws Exception {
		//카테고리
		String reqMedia = request.getParameter("media");
		String reqCategoty = request.getParameter("subname");

		String entries =feedService.getFeedsBasedOnCategory(reqMedia, reqCategoty);

		HttpHeaders resHeaders = new HttpHeaders();
		resHeaders.set("Content-Type", "application/xml;charset=UTF-8");

		return  ResponseEntity.ok().headers(resHeaders).body(entries);
	}
}
