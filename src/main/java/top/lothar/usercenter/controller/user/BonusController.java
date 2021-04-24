package top.lothar.usercenter.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.lothar.usercenter.domain.dto.messaging.UserAddBonusMsgDTO;
import top.lothar.usercenter.domain.dto.user.UserAddBonusDTO;
import top.lothar.usercenter.domain.entity.user.User;
import top.lothar.usercenter.service.user.UserService;

/**
 * <h1>用户积分操作</h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/4/23 17:42
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class BonusController {

    @Autowired
    private UserService userService;

    /**
     * 添加用户积分
     * @param userAddBonusDTO
     * @return
     */
    @PutMapping("/add-bonus")
    public User addBonus(@RequestBody UserAddBonusDTO userAddBonusDTO) {
        Integer userId = userAddBonusDTO.getUserId();
        userService.addBonus(
                UserAddBonusMsgDTO.builder()
                        .userId(userId)
                        .bonus(userAddBonusDTO.getBonus())
                        .description("兑换")
                        .event("BUY")
                        .build()
        );
        return this.userService.findById(userId);
    }


}
