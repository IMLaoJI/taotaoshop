package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ItemParamItemService;

/**
 * 展示商品规格参数
 * <p>Title: ItemParamItemController</p>
 * <p>Description: </p>
* @author      LAOJI
 * @version 1.0
 */
@Controller
public class ItemParamItemController {

	@Autowired
	private ItemParamItemService itemParamItemService;
	
	@RequestMapping("/showitem/{itemId}")
	public String showItemParam(@PathVariable Long itemId, Model model) {
		String string = itemParamItemService.getItemParamByItemId(itemId);
		model.addAttribute("itemParam", string);
		return "item";
	}
	
	@RequestMapping("/getitem/{itemId}")
	 @ResponseBody
	public TaotaoResult showItemParam2(@PathVariable Long itemId) {
	    System.out.println("djsdjs");
	    TaotaoResult result = itemParamItemService.queryOneByID(itemId);
           
            return result;
    }
}
