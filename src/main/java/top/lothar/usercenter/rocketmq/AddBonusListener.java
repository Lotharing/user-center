package top.lothar.usercenter.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.lothar.usercenter.dao.bonus.BonusEventLogMapper;
import top.lothar.usercenter.dao.user.UserMapper;
import top.lothar.usercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import top.lothar.usercenter.domain.entity.bonus.BonusEventLog;
import top.lothar.usercenter.domain.entity.user.User;

import java.util.Date;

/**
 * <h1>审核增加积分的消费者</h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/3/31 19:54
 */
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-bonus")
public class AddBonusListener implements RocketMQListener<UserAddBonusMsgDTO> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BonusEventLogMapper bonusEventLogMapper;

    /**
     * 收到消息的时候执行的方法
     * @param message {@link UserAddBonusMsgDTO}
     */
    @Override
    public void onMessage(UserAddBonusMsgDTO message) {
        // 1.为用户加积分
        Integer userId = message.getUserId();
        Integer bonus = message.getBonus();
        User user = this.userMapper.selectByPrimaryKey(userId);
        user.setBonus(user.getBonus() + bonus);
        this.userMapper.updateByPrimaryKey(user);
        // 2.记录积分日志到 bonus_event_log
        BonusEventLog bonusEventLog = new BonusEventLog();
        bonusEventLog.setUserId(userId);
        bonusEventLog.setCreateTime(new Date());
        bonusEventLog.setDescription("投稿加积分");
        bonusEventLog.setValue(bonus);
        bonusEventLog.setEvent("CONTRIBUTE");
        this.bonusEventLogMapper.insert(bonusEventLog);
    }
}
