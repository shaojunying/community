package com.shao.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shao
 * @date 2020/10/14 20:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTest {
    @Autowired
    private Producer producer;

    @Test
    public void test() throws InterruptedException {
        producer.send("test", "shao");
        producer.send("test", "jun");
        producer.send("test", "ying");

        Thread.sleep(1000 * 10);

    }

}


@Component
class Producer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }
}

@Component
class Consumer {
    @KafkaListener(topics = {"test"})
    public void receive(ConsumerRecord<String, Object> consumerRecord) {
        System.out.println(consumerRecord.value());
    }
}
