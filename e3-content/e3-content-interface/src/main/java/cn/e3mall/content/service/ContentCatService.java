package cn.e3mall.content.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ContentCatService {
	List<EasyUITreeNode> getCategoryList(Long parantId);
	E3Result addContentCat(Long parentId,String name);
	E3Result updateContentCatById(Long id,String name);
	E3Result deleteContentCatById(Long id);
}
