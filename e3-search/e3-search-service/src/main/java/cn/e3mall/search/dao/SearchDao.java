package cn.e3mall.search.dao;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索Dao
 */
@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;

    /**
     * 根据查询条件查询索引库
     */
    public SearchResult search(SolrQuery query) throws Exception {
        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        long recordCount = solrDocumentList.getNumFound();
        SearchResult result = new SearchResult();
        result.setRecordCount(recordCount);
        List<SearchItem> itemList = new ArrayList<>();
        //取高亮集合
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem item = new SearchItem();
            item.setId((String) solrDocument.get("id"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            item.setPrice((Long) solrDocument.get("item_price"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            //如果有高亮内容，将高亮内容封装给对象
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            if(list != null && list.size()>0){
                item.setTitle(list.get(0));
            }else {
                item.setTitle((String) solrDocument.get("item_title"));
            }
            itemList.add(item);
        }
        result.setItemList(itemList);
        return result;
    }
}
