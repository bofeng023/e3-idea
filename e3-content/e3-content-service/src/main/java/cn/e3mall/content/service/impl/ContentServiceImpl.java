package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品内容Service
 * @author bofeng
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	//自定义redis的hash键用于存储分类ID
	private final String CONTENT_LIST = "CONTENT_LIST";
	
	//获取内容列表
	public EasyUIDataGridResult getContentList(Long categoryId, int page, int rows){
		PageHelper.startPage(page, rows);
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	//新增内容
	public E3Result addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	//编辑内容
	public E3Result updateContent(TbContent content) {
		content.setUpdated(new Date());
		contentMapper.updateByPrimaryKeyWithBLOBs(content);
		return E3Result.ok();
	}

	//删除内容
	public E3Result deleteContent(String ids) {
		if(ids.indexOf(",") == -1){
			contentMapper.deleteByPrimaryKey(Long.parseLong(ids));
		}else{
			String[] split = ids.split(",");
			for (String id : split) {
				contentMapper.deleteByPrimaryKey(Long.parseLong(id));
			}
		}
		return E3Result.ok();
	}

	//根据分类ID获取内容列表
	public List<TbContent> getContentListByCid(Long cid) {
		try{
			String json = jedisClient.hget(CONTENT_LIST, cid.toString());
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExample(example);
		try{
			String json = JsonUtils.objectToJson(list);
			jedisClient.hset(CONTENT_LIST, cid.toString(), json);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
