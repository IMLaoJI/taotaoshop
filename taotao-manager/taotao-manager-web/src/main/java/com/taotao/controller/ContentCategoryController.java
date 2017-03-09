package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ContentCategoryService;

/**
 * 内容分类管理
 * <p>Title: ContentCategoryController</p>
 * <p>Description: </p>
* @author      LAOJI
 * @date        2016年12月20日上午9:13:14
 * @version 1.0
 */
@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getContentCatList(@RequestParam(value="id", defaultValue="0")Long parentId) {
		List<EUTreeNode> list = contentCategoryService.getCategoryList(parentId);
		return list;
	}

	    @RequestMapping(value = "/create",method = RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult createContentCategory(long parentId,String name){
	        TaotaoResult taotaoResult = contentCategoryService.insertContentCategory(parentId, name);
	        return taotaoResult;
	    }

	    @RequestMapping(value = "/delete",method = RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult deleteContentCategory(long parentId,long id){
	       // System.out.println(parentId);
	        TaotaoResult taotaoResult=new TaotaoResult();
	       // System.out.println(id);
	       // taotaoResult = contentCategoryService.deleteContentCategory(parentId, id);
	        return taotaoResult;
	    }

	    @RequestMapping(value = "/update",method = RequestMethod.POST)
	    @ResponseBody
	    public TaotaoResult updateContentCategory(long id,String name){
	        TaotaoResult taotaoResult = contentCategoryService.updateContentCategory(id, name);
	        return taotaoResult;
	    }
}
