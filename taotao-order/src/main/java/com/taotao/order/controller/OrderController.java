package com.taotao.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.order.pojo.Order;
import com.taotao.order.pojo.Order2;
import com.taotao.order.pojo.PageResult;
import com.taotao.order.pojo.ResultMsg;
import com.taotao.order.service.OrderService;

@RequestMapping("/order")
@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
        @ResponseBody
        public TaotaoResult createOrder(@RequestBody Order2 order) {
                try {
                    //TaotaoResult result=null;
                        TaotaoResult result = orderService.createOrder1(order, order.getOrderItems(), order.getOrderShipping());
                        return result;
                } catch (Exception e) {
                        e.printStackTrace();
                        return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
                }
        }
	       
	       
	       
	@RequestMapping("test")
        public String showother(Model model) {
               System.out.println("sadsaas");
                
                return "test";
        }
	/**
	 * 根据订单ID查询订单
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/query/{orderId}" ,method = RequestMethod.GET)
	public Order queryOrderById(@PathVariable("orderId") String orderId) {
		return orderService.queryOrderById(orderId);
	}

	/**
	 * 根据用户名分页查询订单
	 * @param buyerNick
	 * @param page
	 * @param count
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/query/{buyerNick}/{page}/{count}")
	public PageResult<Order> queryOrderByUserNameAndPage(@PathVariable("buyerNick") String buyerNick,@PathVariable("page") Integer page,@PathVariable("count") Integer count) {
	        System.out.println("dsadjsjdjsadjsajdjasdj");
		return orderService.queryOrderByUserNameAndPage(buyerNick, page, count);
	}

	
	/**
	 * 修改订单状态
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/changeOrderStatus",method = RequestMethod.POST)
	public TaotaoResult changeOrderStatus(@RequestBody String json) {
		return orderService.changeOrderStatus(json);
	}
}
