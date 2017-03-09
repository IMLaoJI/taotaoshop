package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;

public interface ContentCategoryService {

	List<EUTreeNode> getCategoryList(long parentId);
	TaotaoResult insertContentCategory(long parentId, String name);
	TaotaoResult deleteContentCategory(long parentId, long id);
	TaotaoResult updateContentCategory(long id,String name);
	
}
