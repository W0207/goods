package cn.edu.xmu.goods.mq;


import cn.edu.xmu.ooad.util.JacksonUtil;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RocketMQMessageListener(topic = "log-topic", selectorExpression = "1", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumerGroup = "log-group")
public class Test implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @Override
    public void onMessage(String message) {
        Integer orderId = JacksonUtil.toObj(message, Integer.class);
        logger.info("onMessage: got message orderId =" + orderId + " time = " + LocalDateTime.now());
    }
}
