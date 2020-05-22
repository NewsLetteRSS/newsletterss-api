package dev.newsletterss.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.sun.syndication.io.XmlReader;
import dev.newsletterss.api.entity.Rss;
import dev.newsletterss.api.service.FeedService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.*;

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
@RequestMapping(value = "newsletterssAPI/rss/**")
public class RssFeedController {

	@Autowired
	FeedService feedService;
	@GetMapping("/rssFeedTest")
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
