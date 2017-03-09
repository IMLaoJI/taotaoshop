package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParamItem;

public interface ItemParamItemService {

	String getItemParamByItemId(Long itemId);
	public void updateItemParamItem(Long itemId, String itemParams);
	public TaotaoResult queryOneByID(Long itemId);
}
