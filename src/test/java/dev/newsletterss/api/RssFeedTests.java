package dev.newsletterss.api;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import lombok.extern.java.Log;
import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.json.XML.toJSONObject;

@SpringBootTest
@Log
class RssFeedTests {

	@Test
	void contextLoads() throws IOException, FeedException, JSONException {
		URL feedSource = new URL("https://rss.donga.com/book.xml");
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(feedSource));
		//log.info(feed.getEntries() + "----------------------------------");
		ArrayList<List> rssList = new ArrayList<List>();
		JSONObject json = new JSONObject();
	}

}
