package top.lothar.usercenter.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

/**
 * <h1></h1>
 *
 * @author LuTong.Zhao
 * @Date 2021/4/5 14:49
 */
@Service
@Slf4j
public class MyTestStreamConsumer {

    @StreamListener(MySink.MY_INPUT)
    public void receive(String messageBody){
        log.info("自定义接口消费：通过stream收到了消息: {}", messageBody);
        // 抛一场下边可以处理
        // throw new IllegalArgumentException("XXX");
    }

    @StreamListener("errorChannel")
    public void error(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        System.out.println("Handling ERROR: " + errorMessage);
    }


}
