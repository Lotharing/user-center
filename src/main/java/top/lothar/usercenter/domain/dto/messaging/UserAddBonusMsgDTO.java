package top.lothar.usercenter.domain.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>积分消息体</h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/3/31 17:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddBonusMsgDTO {
    /** 用户ID **/
    private Integer userId;
    /** 加的积分 **/
    private Integer bonus;
    /** 事件 **/
    private String event;
    /** 积分描述 **/
    private String description;
}
