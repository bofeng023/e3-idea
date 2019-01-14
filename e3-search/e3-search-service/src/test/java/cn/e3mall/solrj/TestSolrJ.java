package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestSolrJ {
    /**
     * 添加/更新文档
     * @throws Exception
     */
    @Test
    public void addDocument() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://39.106.171.57:8080/solr");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test003");
        document.addField("item_title","测试商品3");
        document.addField("item_price","299");
        solrServer.add(document);
        solrServer.commit();
    }

    /**
     * 根据id删除文档
     */
    @Test
    public void deleteDocumentById() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://39.106.171.57:8080/solr");
        solrServer.deleteById("test001");
        solrServer.commit();
    }

    /**
     * 根据查询删除文档
     */
    @Test
    public void deleteDocumentByQuery() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://39.106.171.57:8080/solr");
        solrServer.deleteByQuery("item_price:199");
        solrServer.commit();
    }

    /**
     * 简单查询
     */
    @Test
    public void queryDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://39.106.171.57:8080/solr");
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        System.out.println("查询结果的记录数："+solrDocumentList.getNumFound());
        for (SolrDocument solrDocument: solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_price"));
        }
    }

    /**
     * 多条件查询
     */
    @Test
    public void queryDocumentWithConditions() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://39.106.171.57:8080/solr");
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        //过滤条件
        query.setFilterQueries("product_catalog_name:幽默杂货");
        //排序条件
        query.setSort("product_price", SolrQuery.ORDER.asc);
        //分页处理
        query.setStart(0);
        query.setRows(10);
        //结果中域的列表
        query.setFields("id","product_name","product_price","product_catalog_name","product_picture");
        //设置默认搜索域
        query.set("df", "product_keywords");
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        System.out.println("查询结果的记录数："+solrDocumentList.getNumFound());
        for (SolrDocument solrDocument: solrDocumentList) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_price"));
        }
    }

    /**
     * 查询结果高亮显示
     */
    @Test
    public void queryDocumentWithHighLighting() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://39.106.171.57:8080/solr");
        SolrQuery query = new SolrQuery();
        query.setQuery("测试");
        //高亮查询必须设置默认搜索域
        query.set("df", "item_title");
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        System.out.println("查询结果总数："+solrDocumentList.getNumFound());
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        // Map K id V Map
        // Map K 域名称 V List
        for(SolrDocument solrDocument: solrDocumentList){
            Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
            List<String> list = map.get("item_title");
            //判断是否有高亮内容
            if (null != list) {
                System.out.println(list.get(0));
            } else {
                System.out.println(solrDocument.get("item_title"));
            }
        }
    }
}
