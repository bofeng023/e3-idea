package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转Controller
 * @author bofeng
 *
 */
@Controller
public class PageController {
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
	
	//展示商品编辑页面
	@RequestMapping("/rest/page/item-edit")
	public String editItem(){
		return "item-edit";
	}
}
