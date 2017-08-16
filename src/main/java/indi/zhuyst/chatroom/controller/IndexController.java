package indi.zhuyst.chatroom.controller;

import indi.zhuyst.chatroom.pojo.User;
import indi.zhuyst.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by zhuyst on 2017/7/1.
 */
@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String doLogin(HttpSession session, Model model,User loginUser){
        if(loginUser.getUsername().equals("") || loginUser.getUsername() == null){
            model.addAttribute("msg","请输入用户名！");
            return "index";
        }

        User user = userService.login(loginUser);
        if(user == null){
            model.addAttribute("msg","该用户名已登录，请换一个用户名登陆");
            return "index";
        }

        session.setAttribute("user",user);
        return "redirect:/chatRoom";
    }
}
