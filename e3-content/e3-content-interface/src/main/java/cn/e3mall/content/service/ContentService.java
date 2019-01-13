package cn.e3mall.content.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;

import java.util.List;

public interface ContentService {
	EasyUIDataGridResult getContentList(Long categoryId,int page, int rows);
	E3Result addContent(TbContent content);
	E3Result updateContent(TbContent content);
	E3Result deleteContent(String ids);
	List<TbContent> getContentListByCid(Long cid);
}
