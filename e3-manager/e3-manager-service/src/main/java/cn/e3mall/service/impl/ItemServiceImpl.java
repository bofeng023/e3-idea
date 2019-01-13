package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理Service
 * @author 刘康迪
 *
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	public TbItem getItemById(Long itemId) {
		return itemMapper.selectByPrimaryKey(itemId);
	}

	public EasyUIDataGridResult getItemList(int page, int rows) {
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal(total);
		result.setRows(list);
		return result;
	}

	public E3Result addItem(TbItem item, String desc) {
		long itemId = IDUtils.genItemId();
		item.setId(itemId);
		//商品状态1：正常，2：下架，3：删除
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		itemMapper.insert(item);
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		itemDescMapper.insert(itemDesc);
		return E3Result.ok();
	}

	public E3Result showItemDesc(Long itemId) {
		Map<String,String> data = new HashMap<>();
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		data.put("itemDesc", itemDesc.getItemDesc());
		E3Result result = E3Result.build(200, "OK", data);
		return result;
	}

	public E3Result updateItem(TbItem item, String desc) {
		Date date = new Date();
		TbItem preItem = itemMapper.selectByPrimaryKey(item.getId());
		preItem.setUpdated(date);
		preItem.setCid(item.getCid());
		preItem.setImage(item.getImage());
		preItem.setNum(item.getNum());
		preItem.setPrice(item.getPrice());
		preItem.setSellPoint(item.getSellPoint());
		preItem.setTitle(item.getTitle());
		itemMapper.updateByPrimaryKey(preItem);
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(date);
		itemDescMapper.updateByPrimaryKey(itemDesc);
		return E3Result.ok();
	}

	//下架商品
	public E3Result instockItem(String ids) {
		if(ids.indexOf(",") == -1){
			TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(ids));
			//1：正常，2：下架，3：删除
			item.setStatus((byte) 2);
			itemMapper.updateByPrimaryKey(item);
		}else{
			String[] split = ids.split(",");
			for (String itemId : split) {
				TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(itemId));
				item.setStatus((byte) 2);
				itemMapper.updateByPrimaryKey(item);
			}
		}
		return E3Result.ok();
	}
	
	//上架商品
	public E3Result reshelfItem(String ids){
		if(ids.indexOf(",") == -1){
			TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(ids));
			//1：正常，2：下架，3：删除
			item.setStatus((byte) 1);
			itemMapper.updateByPrimaryKey(item);
		}else{
			String[] split = ids.split(",");
			for (String itemId : split) {
				TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(itemId));
				item.setStatus((byte) 1);
				itemMapper.updateByPrimaryKey(item);
			}
		}
		return E3Result.ok();
	}

	//删除商品
	public E3Result deleteItem(String ids) {
		if(ids.indexOf(",") == -1){
			TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(ids));
			//1：正常，2：下架，3：删除
			item.setStatus((byte) 3);
			itemMapper.updateByPrimaryKey(item);
		}else{
			String[] split = ids.split(",");
			for (String itemId : split) {
				TbItem item = itemMapper.selectByPrimaryKey(Long.parseLong(itemId));
				item.setStatus((byte) 3);
				itemMapper.updateByPrimaryKey(item);
			}
		}
		return E3Result.ok();
	}
}
