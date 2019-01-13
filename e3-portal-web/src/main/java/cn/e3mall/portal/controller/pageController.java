package cn.e3mall.portal.controller;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 首页展示Controller
 * @author bofeng
 *
 */
@Controller
public class pageController {
	@Autowired
	private ContentService contentService;
	@Value("${LARGE_AD_CID}")
	private Long LARGE_AD_CID;
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		List<TbContent> ad1List = contentService.getContentListByCid(LARGE_AD_CID);
		model.addAttribute("ad1List", ad1List);
		return "index";
	}
}
