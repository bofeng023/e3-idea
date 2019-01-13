package cn.e3mall.controller;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类Service
 * @author bofeng
 *
 */
@Controller
public class ContentCatController {
	@Autowired
	private ContentCatService contentCatService;
	
	//获取内容分类列表
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getCategoryList(@RequestParam(name="id",defaultValue="0") Long parantId){
		List<EasyUITreeNode> list = contentCatService.getCategoryList(parantId);
		return list;
	}
	
	//添加内容分类
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addContentCat(Long parentId, String name){
		return contentCatService.addContentCat(parentId, name);
	}
	
	//重命名内容分类
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result updateContentCatById(Long id,String name){
		return contentCatService.updateContentCatById(id, name);
	}
	
	//删除内容分类
	@RequestMapping(value="/content/category/delete/",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContentCatById(Long id){
		return contentCatService.deleteContentCatById(id);
	}
}
