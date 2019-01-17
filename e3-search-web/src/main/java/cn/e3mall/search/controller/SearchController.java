package cn.e3mall.search.controller;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 搜索Controller
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String searchItemList(String keyword, @RequestParam(defaultValue = "1") int page, Model model) throws Exception {
        //转换tomcat传过来乱码
        keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
        SearchResult result = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
        model.addAttribute("itemList", result.getItemList());
        model.addAttribute("recordCount", result.getRecordCount());
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("query", keyword);
        //返回逻辑视图
        return "search";
    }
}
