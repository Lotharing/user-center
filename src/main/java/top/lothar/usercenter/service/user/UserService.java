package top.lothar.usercenter.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.lothar.usercenter.dao.user.UserMapper;
import top.lothar.usercenter.domain.entity.user.User;

/**
 * <h1></h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/3/26 16:35
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(Integer id){
        // select * from user where id = id
        return this.userMapper.selectByPrimaryKey(id);
    }
}
