package com.taotao.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.IOrder;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.pojo.Order;
import com.taotao.order.pojo.Order2;
import com.taotao.order.pojo.OrderItem;
import com.taotao.order.pojo.OrderShipping;
import com.taotao.order.pojo.PageResult;
import com.taotao.order.pojo.ResultMsg;
import com.taotao.order.util.ValidateUtil;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

@Service
public class OrderService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private IOrder orderDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    


    private String ORDER_GEN_KEY = "ORDER_GEN_KEY";
 
    private String ORDER_INIT_ID="100557";
   
    private String ORDER_DETAIL_GEN_KEY="ORDER_DETAIL_GEN_KEY";
    
  
    

  
    public Order queryOrderById(String orderId) {
        Order order = orderDao.queryOrderById(orderId);
        return order;
    }

    public PageResult<Order> queryOrderByUserNameAndPage(String buyerNick, int page, int count) {
        return orderDao.queryOrderByUserNameAndPage(buyerNick, page, count);
    }

    public TaotaoResult changeOrderStatus(String json) {
        Order order = null;
        try {
            order = objectMapper.readValue(json, Order.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new TaotaoResult(null, "400", "请求参数有误!");
        }
        return this.orderDao.changeOrderStatus(order);
    }

    
    public TaotaoResult createOrder1(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) throws AmqpException, JsonProcessingException {
        //向订单表中插入记录
        //获得订单号
        System.out.println("dsadsad");
        System.out.println(order);
        String string = jedisClient.get(ORDER_GEN_KEY);
        System.out.println(string);
        if (StringUtils.isBlank(string)) {
                jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
        }
        long orderId = jedisClient.incr(ORDER_GEN_KEY);
        //补全pojo的属性
        order.setOrderId(orderId + "");
        //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        order.setStatus(1);
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        //0：未评价 1：已评价
        order.setBuyerRate(0);
        //向订单表插入数据
        orderMapper.insert(order);
        //插入订单明细
        for (TbOrderItem tbOrderItem : itemList) {
                //补全订单明细
                //取订单明细id
                long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
                tbOrderItem.setId(orderDetailId + "");
                tbOrderItem.setOrderId(orderId + ""); 
                //向订单明细插入记录
                orderItemMapper.insert(tbOrderItem);
        }
        //插入物流表
        //补全物流表的属性
        orderShipping.setOrderId(orderId + "");
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        //发送消息到RabbitMQ
        Map<String, Object> msg = new HashMap<String, Object>(3);
        msg.put("userId", order.getUserId());
        msg.put("orderId", order.getOrderId());
        List<Long> itemIds = new ArrayList<Long>(itemList.size());
        for (TbOrderItem orderItem : itemList) {
            itemIds.add( Long.parseLong(orderItem.getItemId()));
        }
        msg.put("itemIds", itemIds);
        System.out.println(msg);
        this.rabbitTemplate.convertAndSend(objectMapper.writeValueAsString(msg));
        
        return TaotaoResult.ok(orderId);
}

}
