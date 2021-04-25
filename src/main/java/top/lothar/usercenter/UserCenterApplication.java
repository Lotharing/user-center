package top.lothar.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;
import top.lothar.usercenter.rocketmq.MySink;

/**
 * @Project user-center 用户中心
 * @author zhaolutong
 * @Date 2020-03-22 22:10:00
 *
 * {@link MapperScan}扫描MySQL这些包里的接口
 */
@MapperScan("top.lothar.usercenter.dao")
@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding({Sink.class, MySink.class})
@EnableFeignClients
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
