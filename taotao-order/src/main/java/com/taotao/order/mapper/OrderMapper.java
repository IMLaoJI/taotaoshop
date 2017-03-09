package com.taotao.order.mapper;

import java.util.Date;



import org.apache.ibatis.annotations.Param;

import com.taotao.order.pojo.Order;

public interface OrderMapper extends IMapper<Order>{
	
	public void paymentOrderScan(@Param("date") Date date);

}
