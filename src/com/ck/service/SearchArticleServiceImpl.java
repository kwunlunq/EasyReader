package com.ck.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;

import com.ck.pojo.Article;
import com.ck.utils.ElasticUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchArticleServiceImpl implements SearchArticleService {
	
	Client client = ElasticUtil.getClient();
	
	@Autowired
	private CrawlArticleService crawlService;

	private static String INDEX = "es";
	private static String TYPE = "article";
	private static String TYPE_NEWS = "news";
	
	/**
	 * 查詢
	 * @return
	 */
	public SearchResponse search() {
		SearchResponse response = client.prepareSearch(INDEX)
				.setTypes(TYPE)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("test", "test")) // Query
				.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(13)) // filter
				.setFrom(0).setSize(60).setExplain(true)
				.execute()
				.actionGet();
		return response;
	}
	
	public SearchResponse basicSearch() {
		SearchResponse resp = client.prepareSearch().execute().actionGet();
		return resp;
	}
	
	/**
	 * 取得全部資料 
	 * @return
	 */
	public List<Article> getAll() {
		
		System.out.println("取得es中所有資料 開始");
		List<Article> results = new ArrayList<Article>();
		
		// 建立查詢builder
		SearchRequestBuilder srb = client.prepareSearch()
										 .setSize(1000)
										 .setQuery(QueryBuilders.matchAllQuery());
		
		// 使用查詢builder建立多條件查詢 並執行查詢 
		MultiSearchResponse msr = client.prepareMultiSearch()
										.add(srb)
										.execute().actionGet();
		
		// 取出查詢結果 放到結果列表中
		if (msr.getResponses() != null && msr.getResponses().length > 0) {
			for (SearchHit hit : msr.getResponses()[0].getResponse().getHits().getHits()) {
				Article article = null;
				try {
					article = new ObjectMapper().readValue(hit.getSourceAsString(), Article.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
				results.add(article);
			}
		}
		
		System.out.println("取得es中所有資料 結束 共"+results.size()+"筆");
		
		return results;
	}
	
	/**
	 * 依照index/type儲存資料
	 * @param index
	 * @param type
	 * @param json
	 * @return
	 */
	public boolean save(String index, String type, byte[] json) {
		IndexResponse resp = client.prepareIndex(index, type).setSource(json).get();
		return resp.isCreated();
	}
	
	/**
	 * 更新crawler的資料到es
	 */
	public void updateArticles() {
		try {
			// 擷取網站資料
			List<Article> articles = crawlService.crawlArticles();
			
			System.out.println("開始更新資料到es：共"+articles.size()+"筆");
			
			// 儲存擷取的資料到es
			for (Article article : articles) {
				save(INDEX, TYPE_NEWS, new ObjectMapper().writeValueAsBytes(article));
			}
			
			System.out.println("更新完成");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
