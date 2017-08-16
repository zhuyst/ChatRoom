package indi.zhuyst.chatroom.dao;

import indi.zhuyst.chatroom.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface UserDao extends BaseMapper<User> {
}