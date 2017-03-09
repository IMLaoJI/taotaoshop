package com.taotao.search.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.search.mq.service.OrderSearchService;

@RequestMapping("/order")
@Controller
public class OrderSearchController {

    @Autowired
    private OrderSearchService orderSearchService;

    /**
     * 搜索订单
     * 
     * @param keyWorkds 关键字
     * @param userId 用户ID
     * @param page 当前页
     * @param rows 页面大小
     * @return
     */
 /*   @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> search(@RequestParam("keyWords") String keyWords,
            @RequestParam(value="userId", defaultValue = "14") Long userId, @RequestParam("page") Integer page,
            @RequestParam("rows") Integer rows) {
       
        Map<String, Object> result = this.orderSearchService.search(keyWords, page, rows);
        if (null != result) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }*/
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> search(@RequestBody String json) {
       
        Map<String, Object> result = this.orderSearchService.search(json);
        if (null != result) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    @RequestMapping("test")
    public String showother(Model model) {
           System.out.println("sadsaas");
            
            return "test";
    }

}
