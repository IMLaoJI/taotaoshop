package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
* @author      LAOJI
 * @date        2016年12月20日上午9:13:14
 * @version 1.0
 */

@Controller
public class ItemController {
    private static final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
	private ItemService itemService;
	 @Autowired
         private RabbitTemplate rabbitTemplate;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EUDataGridResult getItemList(Integer page, Integer rows) {
		EUDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	private TaotaoResult createItem(TbItem item, String desc, String itemParams) throws Exception {
		TaotaoResult result = itemService.createItem(item, desc, itemParams);
		 try {
	                    // 发送消息通知其它系统，该商品已经更新
	                    Map<String, Object> msg = new HashMap<String, Object>();
	                    msg.put("itemId", item.getId());
	                    msg.put("type", "insert");
	                    msg.put("data", System.currentTimeMillis());
	                    this.rabbitTemplate.convertAndSend("item." + "insert", MAPPER.writeValueAsString(msg));
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
		return result;
	}
	
	    /**
	     * 修改商品信息
	     * 
	     * @param item
	     * @param desc
	     * @return
	     */
	@RequestMapping(value="/item/update", method=RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult updateItem(TbItem item,  String desc,
	             Long itemParamId, String itemParams) {
	     
	    System.out.println(item);
	    System.out.println(desc);
	    TaotaoResult result = this.itemService.updateItem(item, desc,itemParamId,itemParams);
	                System.out.println(item);
	            return result;
	    }
}
