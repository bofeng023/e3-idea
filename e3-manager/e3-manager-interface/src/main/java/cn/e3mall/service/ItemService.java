package cn.e3mall.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {
	TbItem getItemById(Long itemId);
	EasyUIDataGridResult getItemList(int page,int rows);
	E3Result addItem(TbItem item,String desc);
	E3Result showItemDesc(Long itemId);
	E3Result updateItem(TbItem item,String desc);
	E3Result instockItem(String ids);
	E3Result reshelfItem(String ids);
	E3Result deleteItem(String ids);
	TbItemDesc getItemDescById(Long itemId);
}
