package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;
import com.taotao.portal.util.JsonUtil;

/**
 * 调用服务层服务，查询内容列表
 * <p>Title: ContentServiceImpl</p>
 * <p>Description: </p>
* @author      LAOJI
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_INDEX_AD_URL}")
	private String REST_INDEX_AD_URL;
	
	@Override
	public String getContentList() {
		//调用服务层的服务
		String result = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_AD_URL+89);
		//把字符串转换成TaotaoResult
		try {
			TaotaoResult taotaoResult = TaotaoResult.formatToList(result, TbContent.class);
			//取内容列表
			List<TbContent> list = (List<TbContent>) taotaoResult.getData();
			List<Map> resultList = new ArrayList<>();
 			//创建一个jsp页码要求的pojo列表
			for (TbContent tbContent : list) {
				Map map = new HashMap<>();
				map.put("src", tbContent.getPic());
				map.put("height", 240);
				map.put("width", 670);
				map.put("srcB", tbContent.getPic2());
				map.put("widthB", 550);
				map.put("heightB", 240);
				map.put("href", tbContent.getUrl());
				map.put("alt", tbContent.getSubTitle());
				resultList.add(map);
			}
			return JsonUtils.objectToJson(resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 
	 * 首页楼数据
	 */
    @Override
    public String getIndexData() {
        JsonUtil res = JsonUtil.getInstance();
        
        createIndexData(res, "99");
        createIndexData(res, "100");
        createIndexData(res, "101");
        createIndexData(res, "102");
        createIndexData(res, "103");
        return res.toJson();
   
    }
    @SuppressWarnings("unchecked")
    private void createIndexData(JsonUtil res,String dataid){
        //调用服务层的服务
     
        String result = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_AD_URL+dataid);
        //把字符串转换成TaotaoResult
        try {
                TaotaoResult taotaoResult = TaotaoResult.formatToList(result, TbContent.class);
                //取内容列表
                List<TbContent> list = (List<TbContent>) taotaoResult.getData();
               JsonUtil jsonUtil = JsonUtil.getInstance();
           if(list==null)return;
               for (int i = 0; i < list.size(); i++) {
                   TbContent content = list.get(i);
                   jsonUtil.put(
                           String.valueOf(i + 1),
                           JsonUtil.start("d", content.getPic()).put("e", content.getUrl())
                                   .put("c", content.getSubTitle()).put("a", content.getTitleDesc())
                                   .put("b", content.getTitle()).put("f", "1").get());
               }
                res.put(dataid,jsonUtil.get());
        } catch (Exception e) {
                e.printStackTrace();
               
        }
    }

}
