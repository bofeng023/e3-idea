package cn.e3mall.controller;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容Controller
 * @author bofeng
 *
 */
@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	//内容展示
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId, int page, int rows){
		return contentService.getContentList(categoryId, page, rows);
	}
	
	//新增内容
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContent(TbContent content){
		return contentService.addContent(content);
	}
	
	//编辑内容
	@RequestMapping(value="/rest/content/edit",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateContent(TbContent content){
		return contentService.updateContent(content);
	}
	
	//删除内容
	@RequestMapping(value="/content/delete",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContent(String ids){
		return contentService.deleteContent(ids);
	}
}
