package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类Service
 * @author bofeng
 *
 */
@Service
public class ContentCatServiceImpl implements ContentCatService {
	@Autowired
	private TbContentCategoryMapper categoryMapper;
	//获取分类列表
	public List<EasyUITreeNode> getCategoryList(Long parantId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(parantId);
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbContentCategory contentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(contentCategory.getId());
			node.setText(contentCategory.getName());
			node.setState(contentCategory.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}
	
	//添加内容分类
	public E3Result addContentCat(Long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setCreated(new Date());
		contentCategory.setIsParent(false);
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		contentCategory.setUpdated(new Date());
		categoryMapper.insert(contentCategory);
		TbContentCategory parentCat = categoryMapper.selectByPrimaryKey(parentId);
		if(!parentCat.getIsParent()){
			parentCat.setIsParent(true);
			categoryMapper.updateByPrimaryKey(parentCat);
		}
		return E3Result.ok(contentCategory);
	}

	//重命名内容分类
	public E3Result updateContentCatById(Long id, String name) {
		TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		categoryMapper.updateByPrimaryKey(contentCategory);
		return E3Result.ok(contentCategory);
	}

	//删除内容分类
	public E3Result deleteContentCatById(Long id) {
		TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
		TbContentCategoryExample example = new TbContentCategoryExample();
		example.createCriteria().andParentIdEqualTo(contentCategory.getParentId());
		List<TbContentCategory> list = categoryMapper.selectByExample(example);
		if(list.size() == 1){
			TbContentCategory parent = categoryMapper.selectByPrimaryKey(contentCategory.getParentId());
			parent.setIsParent(false);
			categoryMapper.updateByPrimaryKey(parent);
		}
		if(!contentCategory.getIsParent()){
			categoryMapper.deleteByPrimaryKey(id);
			return E3Result.ok();
		}else{
			return E3Result.build(403, "不能删除父分类!");
		}
	}

}
