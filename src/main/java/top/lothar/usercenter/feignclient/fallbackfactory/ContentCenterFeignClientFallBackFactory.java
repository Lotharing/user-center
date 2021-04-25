package top.lothar.usercenter.feignclient.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.lothar.usercenter.domain.dto.user.ShareDTO;
import top.lothar.usercenter.feignclient.ContentCenterFeignClient;

import java.util.List;

/**
 * <h1></h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/3/28 19:56
 */
@Slf4j
@Component
public class ContentCenterFeignClientFallBackFactory implements FallbackFactory<ContentCenterFeignClient> {

    @Override
    public ContentCenterFeignClient create(Throwable cause) {
        return new ContentCenterFeignClient() {

            @Override
            public List<ShareDTO> getMyContributions(Integer userId) {
                return null;
            }
        };
    }
}
