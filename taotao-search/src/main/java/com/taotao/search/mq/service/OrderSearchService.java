package com.taotao.search.mq.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.bean.OrderSearch;
import com.taotao.search.bean.demo;

@Service
public class OrderSearchService {

    @Autowired
    private HttpSolrServer httpSolrServer;
    private static final ObjectMapper objectMapper = new ObjectMapper();

/*    @Value("${TAOTAO_ORDER_SOLR}")
    private String TAOTAO_ORDER_SOLR;*/

/*    public Map<String, Object> search(String keyWords,  Integer page, Integer rows) {
        // 切换到order的core
        System.out.println("kdsdjsandsandljaljdljasj");
        this.httpSolrServer.setBaseURL("http://192.168.64.128:8080/order/collection2");

        try {
            SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
            solrQuery.setQuery("searchField:" + keyWords + " AND userId:" + 14); // 搜索关键词
            // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
            solrQuery.setStart((Math.max(page, 1) - 1) * rows);
            solrQuery.setRows(rows);

            // 执行查询
            QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);

            List<OrderSearch> items = queryResponse.getBeans(OrderSearch.class);
            System.out.println(items);
            // 返回结果集
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("list", items);
            result.put("total", queryResponse.getResults().getNumFound());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/
    public Map<String, Object> search(String json) {
        // 切换到order的core
        demo  dm =new demo();
        System.out.println("kdsdjsandsandljaljdljasj");
        this.httpSolrServer.setBaseURL("http://192.168.64.128:8080/order/collection2");
        try {
            dm = objectMapper.readValue(json, demo.class);
        } catch (Exception e) {
            return (Map<String, Object>) TaotaoResult.build(400, "请求参数有误!");
        }

        try {
            SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
            solrQuery.setQuery("searchField:" + dm.getKeyWords() + " AND userId:" + 14); // 搜索关键词
            // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
            solrQuery.setStart((Math.max(dm.getPage(), 1) - 1) * dm.getRows());
            solrQuery.setRows(dm.getRows());

            // 执行查询
            QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);

            List<OrderSearch> items = queryResponse.getBeans(OrderSearch.class);
            System.out.println(items);
            // 返回结果集
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("list", items);
            result.put("total", queryResponse.getResults().getNumFound());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
