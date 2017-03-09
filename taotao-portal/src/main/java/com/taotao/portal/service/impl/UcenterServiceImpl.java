package com.taotao.portal.service.impl;

import java.util.HashMap;


import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.bean.HttpResult;
import com.taotao.portal.bean.demo;
import com.taotao.portal.service.ApiService;
import com.taotao.portal.service.UcenterService;
import com.taotao.portal.util.JsonUtil;


@Service
public class UcenterServiceImpl implements UcenterService {


   @Autowired
   private ApiService apiService;


   @Value("${TAOTAO_SEARCH_URL}")
   private String TAOTAO_SEARCH_URL;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> queryOrders(Integer page, Integer rows) {
        String url = "http://192.168.64.130:8083/order/query/" + "lisi" + "/" + page
                + "/" + rows;
       
        try {
          
            String jsonData = HttpClientUtil.doGet(url);
            if (jsonData == null) {
                return new HashMap<String, Object>(0);
            }
            return MAPPER.readValue(jsonData, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }
    public Map<String, Object> search(String keyWords, Integer page, Integer rows) {
        String url = "http://192.168.64.130:8081/search/order/search";
           
        demo dm =new demo();
        dm.setKeyWords(keyWords);
        dm.setPage(page);
        dm.setRows(rows);
        String jsonData = HttpClientUtil.doPostJson(url, JsonUtils.objectToJson(dm));
        if (jsonData == null) {
            return new HashMap<String, Object>(0);
        }
      
     //   HttpClientUtil.doPostJson(url, json);
        
        try {
           
            JsonUtil jsonUtil = JsonUtil.getInstance();
           
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            jsonUtil.put("total", jsonNode.get("total").asLong());

            ArrayNode orderIds = (ArrayNode) jsonNode.get("list");
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            for (JsonNode node : orderIds) {
                String orderId = node.get("orderId").asText();
                if(data.containsKey(orderId)){
                    continue;
                }
                // 通过订单号查询订单
                data.put(orderId,queryOrderByOrderId(orderId));
            }
            jsonUtil.put("data", data.values());

            return jsonUtil.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> queryOrderByOrderId(String orderId) {
        String url = "http://192.168.64.130:8083" + "/order/query/" + orderId;
        try {
            String jsonData = HttpClientUtil.doGet(url);
            if (jsonData == null) {
                return new HashMap<String, Object>(0);
            }
            return MAPPER.readValue(jsonData, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Object>(0);
        
    }
}
