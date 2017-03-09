package com.taotao.search.mq.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.search.bean.OrderSearch;

@Component
public class OrderSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSuccessHandler.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

   
    
    @Autowired
    private HttpSolrServer httpSolrServer;



    /**
     * 下单成功后删除对应的购物车操作
     * 
     * @param msg
     * @throws Exception
     */
    public void handler(String msg) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("接收到消息，内容为：{}", msg);
        }
        
        
        System.out.println("dsdsadsadasdsa");
        System.out.println("dsdsadsadasdsa");
        System.out.println("dsdsadsadasdsa");
        System.out.println("dsdsadsadasdsa");
        
        System.out.println("dsdsadsadasdsa");
        System.out.println("dsdsadsadasdsa");
        System.out.println("dsdsadsadasdsa");System.out.println("dsdsadsadasdsa");
        
        //切换到order的core
        this.httpSolrServer.setBaseURL("http://192.168.64.128:8080/order/collection2");

        JsonNode jsonNode = MAPPER.readTree(msg);
        Long userId = jsonNode.get("userId").asLong();
        String orderId = jsonNode.get("orderId").asText();
        System.out.println(userId);
        System.out.println(orderId);

        // 查询订单详情
        String url = "http://192.168.64.130:8083" + "/order/query/" + orderId;
        
        String jsonData = HttpClientUtil.doGet(url);
        System.out.println(jsonData);
        if(null == jsonData){
            return ;
        }
        
        List<OrderSearch> orderSearchs = new ArrayList<OrderSearch>();
        
        JsonNode orders = MAPPER.readTree(jsonData);
        ArrayNode orderItems = (ArrayNode)orders.get("orderItems");
        for (JsonNode orderItem : orderItems) {
            OrderSearch orderSearch = new OrderSearch();
            orderSearch.setCreateTime(System.currentTimeMillis());
            orderSearch.setItemId(orderItem.get("itemId").asText());
            orderSearch.setItemTitle(orderItem.get("title").asText());
            orderSearch.setOrderId(orderId);
            orderSearch.setUserId(userId);
            
            orderSearchs.add(orderSearch);
        }
        
        //写入到solr中
        this.httpSolrServer.addBeans(orderSearchs);
        this.httpSolrServer.commit();

    }

}
