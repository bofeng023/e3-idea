package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 商品搜索Service
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;
    @Value("${DEFAULT_FIELD}")
    private String DEFAULT_FIELD;

    public SearchResult search(String keyword, int page, int rows) throws Exception {
        SolrQuery query = new SolrQuery();
        query.setQuery(keyword);
        if (page < 1) page = 1;
        int start = (page - 1) * rows;
        query.setStart(start);
        query.setRows(rows);
        query.set("df", DEFAULT_FIELD);
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style='color:red'>");
        query.setHighlightSimplePost("</em>");
        SearchResult result = searchDao.search(query);
        //记得加上总页数
        Double totalPage = Math.ceil(result.getRecordCount() / rows);
        result.setTotalPages(totalPage.intValue());
        return result;
    }
}
