package com.ck.service;

import java.util.List;

import com.ck.pojo.Article;

public interface CrawlArticleService {
	
	/**
	 * 到網站上擷取資料
	 * @return List<Article>
	 */
	public List<Article> crawlArticles();
}


