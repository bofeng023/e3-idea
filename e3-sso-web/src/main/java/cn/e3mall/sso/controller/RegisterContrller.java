package cn.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户注册Controller
 */
@Controller
public class RegisterContrller {

    @RequestMapping("page/register")
    public String showRegister(){
        return "register";
    }
}
