package com.ck.test;

import com.ck.pojo.Article;
import com.ck.service.ElasticUtil;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
		
		@SuppressWarnings("unused")
		Article respArticle = new ObjectMapper().configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
				.readValue(resp, Article.class);

		

		ElasticUtil.closeClient();
		System.out.println("finished.");
	}

	public void foo() {
		// Objectma
	}
}
