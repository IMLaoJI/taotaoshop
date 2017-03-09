package com.taotao.portal.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.portal.service.UcenterService;


@RequestMapping("ucenter")
@Controller
public class UcenterController {

    @Autowired
    private UcenterService ucenterService;

    @RequestMapping(value = "my/orders", method = RequestMethod.GET)
    public ModelAndView myOrders(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "6") Integer rows) {
        ModelAndView mv = new ModelAndView("my-orders");
        // 添加模型数据,查询订单
        Map<String, Object> map = this.ucenterService.queryOrders(page, rows);
        mv.addObject("orders", map);
        mv.addObject("page", page);

        // 计算总页数
        Integer total = Integer.valueOf(map.get("totle").toString());
        mv.addObject("totalPage", (total + rows - 1) / rows);
        return mv;
    }

    @RequestMapping(value = "my/orders/search", method = RequestMethod.POST)
    public ModelAndView searchMyOrder(@RequestParam("keyWords") String keyWords,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "6") Integer rows) {
        ModelAndView mv = new ModelAndView("my-orders");
        // 添加模型数据,查询订单
        Map<String, Object> map = this.ucenterService.search(keyWords, page, rows);
        mv.addObject("orders", map);
        mv.addObject("page", page);

        // 计算总页数
        Integer total = Integer.valueOf(map.get("total").toString());
        mv.addObject("totalPage", (total + rows - 1) / rows);
        return mv;
    }
    @RequestMapping("/products/*.html")
    public String showother(Model model) {
           System.out.println("sadsaas");
            
            return "other";
    }

}
