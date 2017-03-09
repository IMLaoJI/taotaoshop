package com.taotao.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.visitor.functions.Insert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;


import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemParamItemService;
import com.taotao.service.ItemService;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
* @author      LAOJI
 * @date        2016年12月20日上午9:13:14
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	    private ItemParamItemService itemParamItemService;
	
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	  private static final ObjectMapper MAPPER = new ObjectMapper();

	    @Autowired
	    private RabbitTemplate rabbitTemplate;
	
	@Override
	public TbItem getItemById(long itemId) {
		
		//TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//添加查询条件
		TbItemExample example = new TbItemExample();
		Criteria criteria1 = example.createCriteria();
		criteria1.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}

	/**
	 * 商品列表查询
	 * <p>Title: getItemList</p>
	 * <p>Description: </p>
	 * @param page
	 * @param rows
	 * @return
	 * @see com.taotao.service.ItemService#getItemList(long, long)
	 */
	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		//查询商品列表
		TbItemExample example = new TbItemExample();
		//分页处理
		example.setOrderByClause("created DESC");
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		//取记录总条数
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}
	

	@Override
	public TaotaoResult createItem(TbItem item, String desc, String itemParam) throws Exception {
		//item补全
		//生成商品ID
//	    System.out.println(item);
//	    System.out.println(desc);
//	    System.out.println(itemParam);
//	    System.out.println("范德萨范德萨");
		Long itemId = IDUtils.genItemId();
//		System.out.println("dsdsa上海华虹");
		item.setId(itemId);
		// '商品状态，1-正常，2-下架，3-删除',
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//插入到数据库
		itemMapper.insert(item);
		System.out.println(item);
		//添加商品描述信息
		TaotaoResult result = insertItemDesc(itemId, desc);
		System.out.println(result);
		if (result.getStatus() != 200) {
			throw new Exception();
		}
		//添加规格参数
		result = insertItemParamItem(itemId, itemParam);
		  // 发送消息
	        sendMsg(item.getId(), "insert");

		if (result.getStatus() != 200) {
			throw new Exception();
		}
		return TaotaoResult.ok();
	}
	
	
	 public TaotaoResult updateItem(TbItem item, String desc,Long itemParamId, String itemParams) {
	        // 强制设置不能更新的字段为null
	        item.setStatus(null);
	        item.setCreated(null);
	    System.out.println(itemParamId);
	    System.out.println(itemParams);
	        System.out.println(item.getId());
	        System.out.println("sadsadsakdska");
	        TbItem record = new TbItem();
                record.setPrice(item.getPrice());
                record.setTitle(item.getTitle());
                record.setSellPoint(item.getSellPoint());
                record.setNum(item.getNum());
                record.setBarcode(item.getBarcode());
                record.setImage(item.getImage());
                record.setUpdated(new Date());
                
                System.out.println(record);
                System.out.println(record.getTitle());
                TbItemExample example3 = new  TbItemExample();
                com.taotao.pojo.TbItemExample.Criteria criteria3 = example3.createCriteria();
                criteria3.andIdEqualTo(item.getId());
                //更新条件
           
                this.itemMapper.updateByExampleSelective(record, example3);
                
                
                
                
	        TbItemDesc record2 = new TbItemDesc();
                record2.setItemDesc(desc);
                record2.setUpdated(new Date());
                TbItemDescExample example2 = new  TbItemDescExample();
                com.taotao.pojo.TbItemDescExample.Criteria criteria = example2.createCriteria();
                criteria.andItemIdEqualTo(item.getId());
                //更新条件
           
                this.itemDescMapper.updateByExampleSelective(record2, example2);
	       

	        // 更新商品描述数据
	       
                // 更新商品规格参数数据
                
                System.out.println(JsonUtils.objectToJson(itemParams));
                TbItemParamItem record4 = new TbItemParamItem();
                record4.setParamData(itemParams);
                record.setUpdated(new Date());
                TbItemParamItemExample example5 = new  TbItemParamItemExample();
                com.taotao.pojo.TbItemParamItemExample.Criteria criteria5 = example5.createCriteria();
                criteria5.andItemIdEqualTo(item.getId());
                //更新条件
           
                this.itemParamItemMapper.updateByExampleSelective(record4, example5);
	     
	       

	        // try {
	        // // 通知其它系统该商品已经更新
	        // String url = this.propertieService.TAOTAO_WEB_URL + "/item/cache/" + item.getId() +
	        // ".html";
	        // this.apiService.doPost(url);
	        // } catch (Exception e) {
	        // // TODO 用邮件或短信通知相关的人检测通知逻辑
	        // e.printStackTrace();
	        // }

	        // 发送消息
	        sendMsg(item.getId(), "update");

	        // return count1 == 1 && count2 == 1 && count3 == 1;
	        // 目前的情况下，并不是所有的商品都有规格参数数据
	        return TaotaoResult.ok();
	    }
	
	

	
	/**
	 * 添加商品描述
	 * <p>Title: insertItemDesc</p>
	 * <p>Description: </p>
	 * @param desc
	 */
	private TaotaoResult insertItemDesc(Long itemId, String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}
	
	/**
	 * 添加规格参数
	 * <p>Title: insertItemParamItem</p>
	 * <p>Description: </p>
	 * @param itemId
	 * @param itemParam
	 * @return
	 */
	private TaotaoResult insertItemParamItem(Long itemId, String itemParam) {
		//创建一个pojo
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParam);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		//向表中插入数据
		itemParamItemMapper.insert(itemParamItem);
		
		return TaotaoResult.ok();
		
	}
	  private void sendMsg(Long itemId, String type) {
	        try {
	            // 发送消息通知其它系统，该商品已经更新
	            Map<String, Object> msg = new HashMap<String, Object>();
	            msg.put("itemId", itemId);
	            msg.put("type", type);
	            msg.put("data", System.currentTimeMillis());
	            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	   

}
