package com.ck.service;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;

import com.ck.pojo.Article;

public interface SearchArticleService {

  public SearchResponse search();
  
  public SearchResponse basicSearch();
  
  /**
   * 取得全部資料 
   * @return
   */
  public List<Article> getAll();
  
  /**
   * 依照index/type儲存資料
   * @param index
   * @param type
   * @param json
   * @return
   */
  public boolean save(String index, String type, byte[] json);
  
  /**
   * 更新crawler的資料到es
   */
  public void updateArticles();
  
}
