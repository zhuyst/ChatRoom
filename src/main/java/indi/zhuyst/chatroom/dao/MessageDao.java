package indi.zhuyst.chatroom.dao;

import indi.zhuyst.chatroom.pojo.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface MessageDao extends BaseMapper<Message> {
    List<Message> listByCondition(@Param("limit") Integer limit);
}