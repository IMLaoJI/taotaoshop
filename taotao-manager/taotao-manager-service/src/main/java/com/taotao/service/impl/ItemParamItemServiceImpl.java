package com.taotao.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.service.ItemParamItemService;

/**
 * 展示商品规格参数
 * <p>Title: ItemParamItemServiceImpl</p>
 * <p>Description: </p>
* @author      LAOJI
 * @date        2016年12月20日上午9:13:14
 * @version 1.0
 */
@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {

	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	
	@Override
	public String getItemParamByItemId(Long itemId) {
		//根据商品id查询规格参数
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		//执行查询
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (list == null || list.size() == 0) {
			return "";
		}
		//取规格参数信息
		TbItemParamItem itemParamItem = list.get(0);
		String paramData = itemParamItem.getParamData();
		//生成html
		// 把规格参数json数据转换成java对象
		List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);
		StringBuffer sb = new StringBuffer();
		sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
		sb.append("    <tbody>\n");
		for(Map m1:jsonList) {
			sb.append("        <tr>\n");
			sb.append("            <th class=\"tdTitle\" colspan=\"2\">"+m1.get("group")+"</th>\n");
			sb.append("        </tr>\n");
			List<Map> list2 = (List<Map>) m1.get("params");
			for(Map m2:list2) {
				sb.append("        <tr>\n");
				sb.append("            <td class=\"tdTitle\">"+m2.get("k")+"</td>\n");
				sb.append("            <td>"+m2.get("v")+"</td>\n");
				sb.append("        </tr>\n");
			}
		}
		sb.append("    </tbody>\n");
		sb.append("</table>");
		return sb.toString();
	}
	

        @Override
	public void updateItemParamItem(Long itemId, String itemParams) {
	        //更新的对象
	        TbItemParamItem record = new TbItemParamItem();
	        record.setParamData(itemParams);
	        record.setUpdated(new Date());
	        TbItemParamItemExample example = new  TbItemParamItemExample();
	        Criteria criteria = example.createCriteria();
                criteria.andItemIdEqualTo(itemId);
	        //更新条件
	   
	        this.itemParamItemMapper.updateByExampleSelective(record, example);
	    }
        
        
        public TaotaoResult queryOneByID(Long itemId) {
            //根据商品id查询规格参数
            TbItemParamItemExample example = new TbItemParamItemExample();
            Criteria criteria = example.createCriteria();
            criteria.andItemIdEqualTo(itemId);
            //执行查询
            List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
            if (list == null || list.size() == 0) {
                    return null;
            }
            //取规格参数信息
            System.out.println(list);
            TbItemParamItem itemParamItem = list.get(0);
            System.out.println(itemParamItem);
            return TaotaoResult.ok(itemParamItem);
        }


}
