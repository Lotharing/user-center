package top.lothar.usercenter.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lothar.usercenter.dao.bonus.BonusEventLogMapper;
import top.lothar.usercenter.dao.user.UserMapper;
import top.lothar.usercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import top.lothar.usercenter.domain.dto.user.LoginRespDTO;
import top.lothar.usercenter.domain.dto.user.UserLoginDTO;
import top.lothar.usercenter.domain.entity.bonus.BonusEventLog;
import top.lothar.usercenter.domain.entity.user.User;

import java.util.Date;

/**
 * <h1></h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/3/26 16:35
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BonusEventLogMapper bonusEventLogMapper;

    public User findById(Integer id){
        // select * from user where id = id
        return this.userMapper.selectByPrimaryKey(id);
    }

    /**
     * 用户登录
     * @param loginDTO
     * @param openId
     * @return
     */
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

    /**
     * 用户积分增加
     * @param msgDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMsgDTO msgDTO) {
        // 1. 为用户加积分
        Integer userId = msgDTO.getUserId();
        Integer bonus = msgDTO.getBonus();
        User user = this.userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + bonus);
        this.userMapper.updateByPrimaryKeySelective(user);

        // 2. 记录日志到bonus_event_log表里面
        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event(msgDTO.getEvent())
                        .createTime(new Date())
                        .description(msgDTO.getDescription())
                        .build()
        );
        log.info("积分添加完毕...");
    }
}
