package cn.e3mall.controller;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理Controller
 * @author 刘康迪
 *
 */
@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		return itemService.getItemById(itemId);
	}
	
	//获取商品列表
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(int page,int rows){
		return itemService.getItemList(page, rows);
	}
	
	//添加商品
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		E3Result result = itemService.addItem(item, desc);
		return result;
	}
	
	//更新商品
	@RequestMapping(value="/rest/item/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateItem(TbItem item,String desc){
		return itemService.updateItem(item, desc);
	}
	
	//展示商品描述
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result showItemDesc(@PathVariable Long itemId){
		E3Result result = itemService.showItemDesc(itemId);
		return result;
	}
	
	//下架商品
	@RequestMapping(value="/rest/item/instock",method=RequestMethod.POST)
	@ResponseBody
	public E3Result instockItem(String ids){
		return itemService.instockItem(ids);
	}
	
	//上架商品
	@RequestMapping(value="/rest/item/reshelf",method=RequestMethod.POST)
	@ResponseBody
	public E3Result reshelf(String ids){
		return itemService.reshelfItem(ids);
	}
	
	//删除商品
	@RequestMapping(value="/rest/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteItem(String ids){
		return itemService.deleteItem(ids);
	}
	
}