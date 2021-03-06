package net.joywise.bigdata.news.handler;

import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import net.joywise.bigdata.news.bean.News;

public class HtmlHander extends BaseHandler {
	private static Logger logger = Logger.getLogger(HtmlHander.class);

	public News neteaseHandler(News n) {
		String html = getContent(n.getUrl());
		if (!html.equals("")) {
			String charset = getCharSet(html);
			try {
				Parser nodeParser = Parser.createParser(html, charset);
				NodeFilter contentFilter = new TagNameFilter("p");
				NodeList contentList = nodeParser.extractAllNodesThatMatch(contentFilter);
				String time = n.getTime(), source = "", title = n.getTitle(), contentString = "";
				try {
					contentString = trimScript(trimStyle(contentList.toHtml()));
				} catch (Exception e) {
					contentString = "";
				}
				if (!title.equals("") || !contentString.equals("")) {
					return new News(n.getUrl(), title, contentString, source, time,n.getCrawl_time(),n.getType());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return new News();
	}
	public News sinaHandler(News n) {
		String html = getContent(n.getUrl());
		if (!html.equals("")) {
			String charset = getCharSet(html);
			try {
				Parser nodeParser = Parser.createParser(html, charset);
				NodeFilter contentFilter = new TagNameFilter("p");
				NodeList contentList = nodeParser.extractAllNodesThatMatch(contentFilter);
				String time = n.getTime(), source = "", title = n.getTitle(), contentString = "";
				try {
					contentString = trimScript(trimStyle(contentList.toHtml()));
				} catch (Exception e) {
					contentString = "";
				}
				if (!title.equals("") || !contentString.equals("")) {
					return new News(n.getUrl(), title, contentString, source, time,n.getCrawl_time(),n.getType());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return new News();
	}
	public News sohuHandler(News n) {
		String html = getContent(n.getUrl());
		if (!html.equals("")) {
			String charset = getCharSet(html);
			try {
				Parser nodeParser = Parser.createParser(html, charset);
				NodeFilter contentFilter = new TagNameFilter("p");
				NodeList contentList = nodeParser.extractAllNodesThatMatch(contentFilter);
				String time = n.getTime(), source = "", title = n.getTitle(), contentString = "";
				try {
					contentString = trimScript(trimStyle(contentList.toHtml()));
				} catch (Exception e) {
					contentString = "";
				}
				if (!title.equals("") || !contentString.equals("")) {
					return new News(n.getUrl(), title, contentString, source, time,n.getCrawl_time(),n.getType());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return new News();
	}
	
	public static void main(String[] args) {
	}
}
