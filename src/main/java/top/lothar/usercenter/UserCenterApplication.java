package top.lothar.usercenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Project user-center 用户中心
 * @author zhaolutong
 * @Date 2020-03-22 22:10:00
 *
 * {@link MapperScan}扫描MySQL这些包里的接口
 */
@MapperScan("top.lothar")
@SpringBootApplication
@EnableDiscoveryClient
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
