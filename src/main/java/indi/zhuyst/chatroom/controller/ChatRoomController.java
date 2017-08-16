package indi.zhuyst.chatroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhuyst on 2017/7/2.
 */
@Controller
public class ChatRoomController {

    @RequestMapping("/chatRoom")
    public String charRoom(HttpServletRequest request, Model model){
        String url = request.getRequestURL().toString();
        url = url.split("//")[1];
        url = url.substring(0 , url.length() - 8);

        model.addAttribute("url",url);
        return "chatRoom";
    }
}
