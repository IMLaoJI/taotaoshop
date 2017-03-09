package com.taotao.service.impl;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemDescService;
@Service
public class ItemDescServiceImpl implements ItemDescService {
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Override
    public TaotaoResult getItemDesc(long itemId) {
          
            //创建查询条件
            TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
            
         
            
            return TaotaoResult.ok(itemDesc);
    }
}
