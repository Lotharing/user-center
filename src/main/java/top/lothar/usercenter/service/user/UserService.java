package top.lothar.usercenter.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.lothar.usercenter.dao.user.UserMapper;
import top.lothar.usercenter.domain.dto.user.LoginRespDTO;
import top.lothar.usercenter.domain.dto.user.UserLoginDTO;
import top.lothar.usercenter.domain.entity.user.User;

import java.util.Date;

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

    public User login(UserLoginDTO loginDTO, String openId){
        User user = this.userMapper.selectOne(
                User.builder().wxId(openId).build()
        );
        // 没有注册过就先注册
        if (user == null){
            User userTosave = User.builder()
                    .wxId(openId)
                    .bonus(300)
                    .wxNickname(loginDTO.getWxNickname())
                    .avatarUrl(loginDTO.getAvatarUrl())
                    .roles("user")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            this.userMapper.insertSelective(userTosave);
            return userTosave;
        }
        // 已经注册过
        return user;
    }
}
