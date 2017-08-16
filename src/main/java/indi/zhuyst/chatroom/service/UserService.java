package indi.zhuyst.chatroom.service;

import indi.zhuyst.chatroom.dao.UserDao;
import indi.zhuyst.chatroom.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private static List<String> loginUsers = new ArrayList<>();

    @Autowired
    private UserDao userDao;

    public User addNewUser(User user){
        int result = userDao.insertSelective(user);
        if(result > 0){
            return user;
        }
        return null;
    }

    public User login(User loginUser){
        User user;
        //如果用户已登录，则return null
        if(loginUsers.contains(loginUser.getUsername())){
            return null;
        }

        //根据username获取用户
        user = getUserByUsername(loginUser.getUsername());
        //如果没获取到user，则新增一个user
        if(user == null){
            user = addNewUser(loginUser);
        }

        return user;
    }

    public List<String> getLoginUsernames(){
        return loginUsers;
    }

    public void addLoginUsername(String username){
        loginUsers.add(username);
    }

    public void removeLoginUsername(String username){
        loginUsers.remove(username);
    }

    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        return userDao.selectOne(user);
    }

    public User getUserByUid(Integer uid){
        return userDao.selectByPrimaryKey(uid);
    }
}
