package indi.zhuyst.chatroom.websocket;

import indi.zhuyst.chatroom.enums.MessageTypeEnum;

import java.util.Date;

/**
 * Created by zhuyst on 2017/7/3.
 */
public class ChatRoomMessage {
    private String username;

    private MessageTypeEnum type;

    private String message;

    private Date date;

    ChatRoomMessage() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageTypeEnum getType() {
        return type;
    }

    public void setType(MessageTypeEnum type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
