package com.shao.community.event;

import com.alibaba.fastjson.JSONObject;
import com.shao.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author shao
 * @date 2020/10/16 19:17
 */
@Component
public class ProduceEvent {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * 发布事件
     *
     * @param event the event
     */
    public void fireEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
