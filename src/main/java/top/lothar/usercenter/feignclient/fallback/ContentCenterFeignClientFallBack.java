package top.lothar.usercenter.feignclient.fallback;

import org.springframework.stereotype.Component;
import top.lothar.usercenter.domain.dto.user.ShareDTO;
import top.lothar.usercenter.feignclient.ContentCenterFeignClient;

import java.util.List;

/**
 * <h1></h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/3/28 19:44
 */
@Component
public class ContentCenterFeignClientFallBack implements ContentCenterFeignClient {

    @Override
    public List<ShareDTO> getMyContributions(Integer userId) {
        return null;
    }

    @Override
    public List<ShareDTO> myExchange(Integer userId) {
        return null;
    }
}
