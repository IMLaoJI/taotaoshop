package com.taotao.search.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.search.bean.Item;

@Component
public class ItemHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private HttpSolrServer httpSolrServer;

   

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemHandler.class);

    /**
     * 处理商品消息
     * 
     * @param msg
     * @throws Exception
     * @throws
     */
    public void handler(String msg) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("接收到的消息为：{}", msg);
        }
        
      
        try {
            
            JsonNode jsonNode = MAPPER.readTree(msg);
            String type = jsonNode.get("type").asText();
            Long id = jsonNode.get("itemId").asLong();
            if (StringUtils.equals(type, "update") || StringUtils.equals(type, "insert")) {
                // 从后台管理系统中查询最新的商品数据
                String url = "http://192.168.64.130:8080/rest/item/info/" + id;
                String jsonData = HttpClientUtil.doGet(url);
                System.out.println(jsonData);
                
                TaotaoResult item1 = JsonUtils.jsonToPojo(jsonData,TaotaoResult.class);
                System.out.println(item1);
                System.out.println(item1.getData());
                
               /* JsonNode data = MAPPER.readTree(jsonData);
                String dsdsa=data.get("data").asText();
                System.out.println(dsdsa);*/
                Item item = MAPPER.readValue(JsonUtils.objectToJson(item1.getData()), Item.class);
                System.out.println(item);
                
               
                    //创建一个SolrInputDocument对象
                    SolrInputDocument document = new SolrInputDocument();
                    document.setField("id", item.getId());
                    document.setField("item_title", item.getTitle());
                    document.setField("item_sell_point", item.getSellPoint());
                    document.setField("item_price", item.getPrice());
                    document.setField("item_image", item.getImage());
                    document.setField("item_category_name", item.getCid());
                    
                    
                    //写入索引库
                    this.httpSolrServer.add(document);
            
            //提交修改
                
                this.httpSolrServer.commit();
            } else if (StringUtils.equals(type, "delete")) {
                this.httpSolrServer.deleteById(String.valueOf(id));//删除
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
