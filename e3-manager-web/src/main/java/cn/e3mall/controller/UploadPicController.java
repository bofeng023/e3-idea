package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传Controller
 * @author bofeng
 *
 */
@Controller
public class UploadPicController {
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadPic(MultipartFile uploadFile){
		Map result = new HashMap<>();
		try {
			String originalFilename = uploadFile.getOriginalFilename();
			String ext = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			FastDFSClient client = new FastDFSClient("classpath:conf/client.properties");
			String url = client.uploadFile(uploadFile.getBytes(), ext);
			url = IMAGE_SERVER_URL + url;
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("message", "图片上传失败!");
			return JsonUtils.objectToJson(result);
		}
	}
}
