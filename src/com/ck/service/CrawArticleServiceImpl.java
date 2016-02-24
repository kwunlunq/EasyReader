package com.ck.service;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ck.pojo.Article;

public class CrawArticleServiceImpl {
	
	public  List<Article> updateArticles() throws Exception {
		List<Article> articles = queryArticleTitle();
		articles = queryArticleContent(articles);
		return articles;
	}
	
	public  List<Article> queryArticleTitle() throws Exception {
		
		final URL url = new URL("https://tw.news.yahoo.com/society/archive/");
		
		Document doc = Jsoup.parse(url, 3000);
		Element element = doc.select("ul.yom-list-wide").first();
		Iterator<Element> ite = element.select("div.txt").iterator();
		List<Article> list = new ArrayList<Article>();
		
		while (ite.hasNext()) {
			
			Element ele = ite.next();
			Element title = ele.select("h4 a").first();
			Element date = ele.select("cite").first();
			
			// 取新聞時間
			Matcher matcher = Pattern.compile("(2.*)").matcher(date.text());
			matcher.find();
			String dateStr = matcher.group(0);
			
			// 取id
			Matcher matcherId = Pattern.compile("([0-9]{9})").matcher(
					title.attr("href"));
			matcherId.find();

			DateFormat df = new SimpleDateFormat("yyyy年mm月dd日 ahh:mm");
			Article article = new Article();
			
			article.setId(Integer.parseInt(matcherId.group(0)));
			article.setTitle(title.text());
			article.setHref(title.attr("href"));
			article.setDate(df.parse(dateStr));
			list.add(article);
		}
		
		return list;
	}

	public  List<Article> queryArticleContent(List<Article> newList)
			throws Exception {

		for (Article article : newList) {
			final URL url = new URL("https://tw.news.yahoo.com" + article.getHref());
			Document doc = Jsoup.parse(url, 3000);
			Element element = doc.select(".yom-art-content ").first();
			article.setContent(element.text());
		}
		
		return newList;
	}

	public static void main(String[] args) throws Exception {
		
		System.out.println("Start");
		long s = System.currentTimeMillis();
		
		CrawArticleServiceImpl service = new CrawArticleServiceImpl();
		List<Article> listTitle = service.queryArticleTitle();
		listTitle = service.queryArticleContent(listTitle);
		
		System.out.println((System.currentTimeMillis() - s) + " ms");
		
		System.out.println("---------insert----------");
//		articleDB.insert(listTitle);
		
	}
}


