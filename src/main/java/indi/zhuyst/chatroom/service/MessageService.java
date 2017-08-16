package indi.zhuyst.chatroom.service;

import indi.zhuyst.chatroom.dao.MessageDao;
import indi.zhuyst.chatroom.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    public boolean addMessage(Message message){
        return messageDao.insertSelective(message) > 0;
    }

    public List<Message> listMessage(){
        List<Message> list =  messageDao.listByCondition(10);
        Collections.reverse(list);
        return list;
    }
}
