package top.lothar.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1></h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/4/20 22:24
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginRespDTO {
    /** token **/
    private JwtTokenRespDTO token;
    /** 用户信息 **/
    private UserRespDTO user;
}
