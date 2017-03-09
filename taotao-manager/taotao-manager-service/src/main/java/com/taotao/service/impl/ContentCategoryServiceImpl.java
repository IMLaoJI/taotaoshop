package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCategoryService;

/**
 * 内容分类管理
 * <p>Title: ContentCategoryServiceImpl</p>
 * <p>Description: </p>
* @author      LAOJI
 * @date        2016年12月20日上午9:13:14
 * @version 1.0
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Override
	public List<EUTreeNode> getCategoryList(long parentId) {
		//根据parentid查询节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EUTreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			//创建一个节点
			EUTreeNode node = new EUTreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			
			resultList.add(node);
		}
		return resultList;
	}
	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		
		//创建一个pojo
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setIsParent(false);
		//'状态。可选值:1(正常),2(删除)',
		contentCategory.setStatus(1);
		contentCategory.setParentId(parentId);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//添加记录
		contentCategoryMapper.insert(contentCategory);
		//查看父节点的isParent列是否为true，如果不是true改成true
		TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
		//判断是否为true
		if(!parentCat.getIsParent()) {
			parentCat.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		//返回结果
		return TaotaoResult.ok(contentCategory);
	}
	 @Override
	    public TaotaoResult deleteContentCategory(long parentId, long id) {
	        // 先根据id查找对应记录
	     System.out.println("dsadsjadjsajdjsa");
	        TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
	        System.out.println(tbContentCategory);
	       
	            // 如果是子节点,直接删除
	            contentCategoryMapper.deleteByPrimaryKey(id);
	            // 判断parentId对应的记录下是否有子节点,如果没有子节点,就把isParent改成false
	            TbContentCategoryExample example = new TbContentCategoryExample();
	            TbContentCategoryExample.Criteria criteria = example.createCriteria();
	            criteria.andIdEqualTo(parentId);
	            List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
	            TbContentCategory parentCat = list.get(0);
	            if (list.size() > 0 && list.size() == 1) {
	                // 没有子节点
	                parentCat.setIsParent(false);
	                contentCategoryMapper.updateByPrimaryKeySelective(parentCat);
	            }
	        
	        return TaotaoResult.ok();
	    }
	 @Override
	    public TaotaoResult updateContentCategory(long id, String name) {
	        TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
	        System.out.println(tbContentCategory);
	        if (tbContentCategory != null) {
	            System.out.println(tbContentCategory);
	            tbContentCategory.setName(name);
	            contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
	            return TaotaoResult.ok();
	        } else {
	            return TaotaoResult.build(400,"更新失败");
	        }
	    }


}
