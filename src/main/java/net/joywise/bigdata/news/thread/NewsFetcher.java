package net.joywise.bigdata.news.thread;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import net.joywise.bigdata.news.bean.News;
import net.joywise.bigdata.news.client.HttpClient;
import net.joywise.bigdata.news.client.RedisClient;
import net.joywise.bigdata.news.handler.JsonHandler;
import net.joywise.bigdata.news.handler.UrlMap;

public class NewsFetcher implements Runnable {

	private List<String> urlSeeds = new ArrayList<String>();
	private HttpClient client = new HttpClient();
	private UrlMap map = UrlMap.getInstance();
	private static Logger logger = Logger.getLogger(NewsFetcher.class);
	private final String NETEASE = "netease";
	private final String SINA = "sina";
	private final String SOHU = "sohu";
	private final String CONFIG_SPLIT = "\t";

	public void run() {
		while (true) {
			// fetch news from website
			List<News> news = new ArrayList<News>();
			try {
				for (String u : urlSeeds) {
					String newsType[] = u.split(CONFIG_SPLIT);
					if (newsType[0].equals(NETEASE)) {
						String url = formatSeedUrl(newsType[1], newsType[0]);
						String neteaseContent = client.getContent(url,"gbk");
						List<News> newsNetease = JsonHandler.neteaseHandler(neteaseContent);
						news.addAll(newsNetease);
					} else if (newsType[0].equals(SINA)) {
						String url = formatSeedUrl(newsType[1], newsType[0]);
						String sinaContent = client.getContent(url,"gbk");
						List<News> newsSina = JsonHandler.sinaHandler(sinaContent);
						news.addAll(newsSina);
					} else if (newsType[0].equals(SOHU)) {
						String url = formatSeedUrl(newsType[1], newsType[0]);
						String sohuContent = client.getContent(url,"utf-8");
						List<News> newsSohu = JsonHandler.sohuHandler(sohuContent);
						news.addAll(newsSohu);
					}
					logger.info("news size:" + news.size());
					for (News n : news) {
						if (!map.isFetched(n.getUrl())) {
							RedisClient.rpush("url_fetch", n);
						}
						map.addUrl(n.getUrl());
					}
				}
				logger.info("request news thread end,after 2 min repeat!");
				logger.info("Map size:" + map.size());
				Thread.sleep(1000 * 120);
			} catch (InterruptedException e) {
				logger.error("NewsFetcher Thread Exception:" + e.getMessage());
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				logger.error("NewsFetcher Thread Exception:" + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("NewsFetcher Thread Exception:" + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	public void addSeed(String url) {
		urlSeeds.add(url);
	}

	public List<String> getSeeds() {
		return urlSeeds;
	}

	private String formatSeedUrl(String url, String type) {
		if (type.equals(SINA)) {
			return url;
		}
		if (type.equals(NETEASE)) {
			return url;
		}
		if (type.equals(SOHU)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			return url.replace("{0}", sdf.format(new Date()));
		}
		return url;
	}
}