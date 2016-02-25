package com.ck.driver.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ck.pojo.Article;
import com.ck.service.SearchArticleService;
import com.ck.utils.ElasticUtil;

public class ElasticMain {

	
	public static SearchArticleService articleService;

	public static void main(String[] args) throws Exception {
		
		System.out.println();

		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
		
		articleService = context.getBean(SearchArticleService.class);

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
