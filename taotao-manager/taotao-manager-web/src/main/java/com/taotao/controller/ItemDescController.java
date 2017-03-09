package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ItemDescService;



@RequestMapping("item")
@Controller
public class ItemDescController {

    @Autowired
    private ItemDescService itemDescService;

    /**
     * 根据商品id查询商品描述数据
     * 
     * @param itemId
     * @return
     */
    @RequestMapping("/desc/{itemId}")
    @ResponseBody
    public TaotaoResult getItemDesc(@PathVariable Long itemId) {
            TaotaoResult result = itemDescService.getItemDesc(itemId);
            return result;
    }
}
