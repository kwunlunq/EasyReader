package com.ck.driver.test;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;

import com.ck.pojo.Article;
import com.ck.service.SearchArticleServiceImpl;
import com.ck.utils.ElasticUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticMain {

	private static String INDEX = "es";
	private static String TYPE = "article";

	public static void main(String[] args) throws Exception {

		// 建立client
		ElasticUtil.getClient();

		// 建立mapper
		ObjectMapper mapper = new ObjectMapper();
		Article article = new Article();
		article.setAuthor("chien");
		article.setCategory(5);
		article.setContent("20");
		byte[] json = mapper.writeValueAsBytes(article);

		ElasticUtil.createIndex(INDEX, TYPE, "1", json);
		String resp = ElasticUtil.getById(INDEX, TYPE, "1");
		
		System.out.println(resp);
		
//		@SuppressWarnings("unused")
//		Article respArticle = new ObjectMapper().configure(
//				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//				.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
//				.readValue(resp, Article.class);
		
		SearchArticleServiceImpl service = new SearchArticleServiceImpl();
//		SearchResponse searchResp = service.basicSearch();
		List<SearchResponse> allData = service.getAll();
		for (SearchResponse sp : allData) {
			String source = sp.getHits().getAt(0).getSourceAsString();
			Article article1 = new ObjectMapper().readValue(source, Article.class);
			System.out.println();
			// System.out.println(sp.get);
		}

		ElasticUtil.closeClient();
		System.out.println("finished.");
	}

	public void foo() {
		// Objectma
	}
}
