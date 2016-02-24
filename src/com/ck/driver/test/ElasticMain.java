package com.ck.driver.test;

import java.util.List;

import com.ck.pojo.Article;
import com.ck.service.SearchArticleServiceImpl;
import com.ck.utils.ElasticUtil;

public class ElasticMain {

	public static SearchArticleServiceImpl articleService = new SearchArticleServiceImpl();

	public static void main(String[] args) throws Exception {

		// ApplicationContext context = new ClassPathXmlApplicationContext( "Beans.xml");

		testSave();

		ElasticUtil.closeClient();
		System.out.println("finished.");
	}

	public static void testSave() {

		articleService.updateArticles();

		List<Article> articles = articleService.getAll();

		for (int i = 0; i < articles.size(); i++) {
			System.out.println(i + ". " + articles.get(i).getTitle());
		}

	}

}
