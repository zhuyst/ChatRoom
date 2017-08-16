package indi.zhuyst.chatroom.websocket;

import com.alibaba.fastjson.JSON;
import indi.zhuyst.chatroom.enums.MessageTypeEnum;
import indi.zhuyst.chatroom.pojo.Message;
import indi.zhuyst.chatroom.pojo.User;
import indi.zhuyst.chatroom.service.MessageService;
import indi.zhuyst.chatroom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuyst on 2017/7/1.
 */
public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
    private static Map<Integer,WebSocketSession> users = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get("user");

        users.put(user.getUid(),session);
        userService.addLoginUsername(user.getUsername());

        //向所有用户广播 - 用户进入聊天室
        String message = "用户\t" + user.getUsername() + "\t进入聊天室，当前用户数为" + users.size();
        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setUsername(user.getUsername());
        chatRoomMessage.setType(MessageTypeEnum.in);
        chatRoomMessage.setMessage(message);
        chatRoomMessage.setDate(new Date());

        logger.info(message);
        sendAll(chatRoomMessage);
        sendHistoryMessage(session);
        sendLoginState(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        User user = (User) session.getAttributes().get("user");
        String returnMessage = message.getPayload().toString().trim();

        //java.nio.HeapByteBuffer是为了保持socket连接的信息，不用进行处理
        if(returnMessage.contains("java.nio.HeapByteBuffer")){
            return;
        }

        //向所有用户广播 - 聊天信息
        returnMessage = returnMessage.replace("\n","<br/>");
        logger.info(user.getUsername()
                + "\t说:\t" + returnMessage);

        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setUsername(user.getUsername());
        chatRoomMessage.setMessage(returnMessage);
        chatRoomMessage.setDate(new Date());
        sendAllByMe(user,chatRoomMessage);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        //当用户发生连接错误或者非正常关闭聊天室时，移除用户
        User user = (User) session.getAttributes().get("user");
        if(session.isOpen()){
            session.close();
        }
        logger.info("用户\t" + user.getUsername() + "\t发生连接错误，连接关闭");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = (User) session.getAttributes().get("user");

        users.remove(user.getUid());
        userService.removeLoginUsername(user.getUsername());

        //向所有用户广播 - 用户退出聊天室
        String message = "用户\t" + user.getUsername() + "\t退出了聊天室,剩余用户数为" + users.size();
        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setUsername(user.getUsername());
        chatRoomMessage.setType(MessageTypeEnum.out);
        chatRoomMessage.setMessage(message);
        chatRoomMessage.setDate(new Date());

        logger.info(message);
        sendAll(chatRoomMessage);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    //向所有用户发送公共信息
    private void sendAll(ChatRoomMessage message) throws IOException {
        TextMessage returnMessage = new TextMessage(JSON.toJSONString(message));

        for(Map.Entry<Integer,WebSocketSession> user : users.entrySet()){
            user.getValue().sendMessage(returnMessage);
        }
    }

    //聊天用，向其他用户发送other信息，向自己发送me信息，用于区分是否自己发送的信息
    private void sendAllByMe(User sendUser,ChatRoomMessage message) throws IOException {
        Message sqlMessage = new Message();
        sqlMessage.setUid(sendUser.getUid());
        sqlMessage.setMessage(message.getMessage());
        sqlMessage.setDate(message.getDate());
        messageService.addMessage(sqlMessage);

        message.setType(MessageTypeEnum.my);
        TextMessage returnMessage = new TextMessage(JSON.toJSONString(message));
        users.get(sendUser.getUid()).sendMessage(returnMessage);

        message.setType(MessageTypeEnum.other);
        returnMessage = new TextMessage(JSON.toJSONString(message));
        for(Map.Entry<Integer,WebSocketSession> user : users.entrySet()){
            if(!user.getKey().equals(sendUser.getUid())){
                user.getValue().sendMessage(returnMessage);
            }
        }
    }

    private void sendMe(WebSocketSession session,ChatRoomMessage message) throws IOException {
        TextMessage textMessage = new TextMessage(JSON.toJSONString(message));
        session.sendMessage(textMessage);
    }

    private void sendHistoryMessage(WebSocketSession session) throws IOException {
        User user = (User) session.getAttributes().get("user");

        List<Message> list = messageService.listMessage();
        for(Message message : list){
            ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
            User sendUser = userService.getUserByUid(message.getUid());

            chatRoomMessage.setUsername(sendUser.getUsername());
            chatRoomMessage.setMessage(message.getMessage());
            chatRoomMessage.setDate(message.getDate());
            if(message.getUid().equals(user.getUid())){
                chatRoomMessage.setType(MessageTypeEnum.my);
            }
            else {
                chatRoomMessage.setType(MessageTypeEnum.other);
            }

            sendMe(session,chatRoomMessage);
        }
    }

    private void sendLoginState(WebSocketSession session) throws IOException {
        StringBuilder message = new StringBuilder();
        for(String username : userService.getLoginUsernames()){
            message.append(username).append("，");
        }
        message.deleteCharAt(message.length() - 1);
        message.append("。");

        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setUsername("当前聊天室中登陆的用户有：");
        chatRoomMessage.setMessage(message.toString());
        chatRoomMessage.setType(MessageTypeEnum.state);
        chatRoomMessage.setDate(new Date());

        sendMe(session,chatRoomMessage);
    }
}
