package com.ck.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.ck.utils.ElasticUtil;

public class SearchArticleServiceImpl implements SearchArticleService {
	
	Client client = ElasticUtil.getClient();

	private static String INDEX = "es";
	private static String TYPE = "article";
	
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
	
	public List<SearchResponse> getAll() {
		List<SearchResponse> results = new ArrayList<SearchResponse>();
		SearchRequestBuilder srb1 = client.prepareSearch().setQuery(QueryBuilders.matchAllQuery());
		MultiSearchResponse sr = client.prepareMultiSearch()
				.add(srb1)
				.execute().actionGet();
		for (MultiSearchResponse.Item item : sr.getResponses()) {
			// SearchResponse resp = item.getResponse();
			results.add(item.getResponse());
		}
		return results;
	}
}
