package top.lothar.usercenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.lothar.usercenter.domain.dto.user.ShareDTO;
import top.lothar.usercenter.feignclient.fallback.ContentCenterFeignClientFallBack;
import top.lothar.usercenter.feignclient.fallbackfactory.ContentCenterFeignClientFallBackFactory;

import java.util.List;

/**
 * <h1>Feign远程内容中心接口</h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/4/24 23:17
 */
@FeignClient(name = "content-center",configuration = ContentCenterFeignClientFallBack.class,
        fallbackFactory = ContentCenterFeignClientFallBackFactory.class)
public interface ContentCenterFeignClient {

    /**
     * 内容中心-我的投稿
     * http://content-center/users/{id}
     * @param userId
     * @return
     */
    @GetMapping("/shares/contributions")
    List<ShareDTO> getMyContributions(@RequestParam Integer userId);
}
