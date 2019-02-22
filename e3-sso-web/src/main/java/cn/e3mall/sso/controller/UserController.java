package cn.e3mall.sso.controller;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户注册Controller
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("page/register")
    public String showRegister() {
        return "register";
    }

    @RequestMapping("/page/login")
    public String showLogin() {
        return "login";
    }

    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
        E3Result result = userService.checkData(param, type);
        return result;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public E3Result register(TbUser user) {
        E3Result result = userService.register(user);
        return result;
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest request,
                          HttpServletResponse response) {
        E3Result result = userService.login(username, password);
        //如果登录成功，将用户信息写入cookie
        if (result.getStatus() == 200) {
            String token = (String) result.getData();
            CookieUtils.setCookie(request, response, "token", token);
        }
        return result;
    }

    @RequestMapping(value = "/user/token/{token}", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {
        E3Result result = userService.getUserByToken(token);
        //响应结果之前，判断是否为jsonp请求
        if (StringUtils.isNotBlank(callback)) {
            //把结果封装成一个js语句响应
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }
        return JsonUtils.objectToJson(result);
    }


}
