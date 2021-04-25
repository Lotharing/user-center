package top.lothar.usercenter.controller.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.lothar.usercenter.auth.CheckLogin;
import top.lothar.usercenter.domain.dto.user.*;
import top.lothar.usercenter.domain.entity.user.User;
import top.lothar.usercenter.feignclient.ContentCenterFeignClient;
import top.lothar.usercenter.service.user.UserService;
import top.lothar.usercenter.util.JwtOperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1></h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/3/26 16:36
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private JwtOperator jwtOperator;

    @Autowired
    private ContentCenterFeignClient contentCenterFeignClient;

    @GetMapping("/{id}")
    @CheckLogin
    public User findById(@PathVariable Integer id){
        log.info("接口: {"+"用户中心/user/id"+"}请求成功～");
        return this.userService.findById(id);
    }

    /**
     * 模拟生成token(假的登录)
     */
    @GetMapping("/gen-token")
    public String genToken() {
        Map<String, Object> userInfo = new HashMap<>(3);
        userInfo.put("id", 1);
        userInfo.put("wxNickname", "LT");
        userInfo.put("role", "admin");
        return this.jwtOperator.generateToken(userInfo);
    }

    /**
     * 小程序请求完微信得到「code avatarUrl wxNickname」
     * 用code请求微信API验证是否登陆
     * 注册颁发token返回用户信息
     * 未注册，注册 颁发token 返回用户信息
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody UserLoginDTO loginDTO) throws WxErrorException {
        // 微信小程序服务端校验是否已经登录的结果
        WxMaJscode2SessionResult result = this.wxMaService.getUserService().getSessionInfo(loginDTO.getCode());
        // 微信的openId,用户在微信的唯一标示
        String openid = result.getOpenid();
        // 看用户是否注册「注册颁发token，没注册先注册」
        User user = this.userService.login(loginDTO, openid);
        // 颁发token
        Map<String,Object> userInfo = new HashMap<>(3);
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("role", user.getRoles());
        String token = jwtOperator.generateToken(userInfo);
        log.info("用户{}登录成功, 生成的token: {}, 有效期到：{}", loginDTO.getWxNickname(), token, jwtOperator.getExpirationTime());
        // 构建响应
        return LoginRespDTO.builder()
                .user(
                        UserRespDTO.builder()
                        .id(user.getId())
                        .avatarUrl(user.getAvatarUrl())
                        .bonus(user.getBonus())
                        .wxNickname(user.getWxNickname())
                        .build()
                )
                .token(
                        JwtTokenRespDTO.builder()
                        .expirationTime(jwtOperator.getExpirationTime().getTime())
                        .token(token)
                        .build()
                )
                .build();
    }

    /**
     * 个人主页信息展示
     * @return
     */
    @CheckLogin
    @GetMapping("/me")
    public User getMe(@RequestHeader("X-Token")String token){
        User user = null;
        if (token != null){
            Claims claims = jwtOperator.getClaimsFromToken(token);
            Integer userId = (Integer) claims.get("id");
            user = this.userService.findById(userId);
        }
        return user;
    }

    /**
     * 我的投稿-内容中心接口
     * @param token
     * @return
     */
    @GetMapping("/contributions")
    public List<ShareDTO> myContributions(@RequestHeader("X-Token")String token){
        Integer userId = null;
        if (StringUtils.isNotBlank(token)) {
            Claims claims = jwtOperator.getClaimsFromToken(token);
            userId = (Integer) claims.get("id");
        }
        return contentCenterFeignClient.getMyContributions(userId);
    }

}
