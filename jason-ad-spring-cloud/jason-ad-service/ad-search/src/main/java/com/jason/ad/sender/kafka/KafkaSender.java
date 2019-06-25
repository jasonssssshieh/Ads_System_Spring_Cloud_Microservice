package com.jason.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.jason.ad.mysql.dto.MySqlRowData;
import com.jason.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("kafkaSender")
@Slf4j
public class KafkaSender implements ISender{

    @Value("${adconf.kafka.topic")
    private String topic;//配置文件里定义 kafkaSender会去获取配置文件中的定义

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    //这里kafkaTemplate会报错  但根据我查过的资料 这里应该是okay 的
    //https://stackoverflow.com/questions/21323309/intellij-idea-shows-errors-when-using-springs-autowired-annotation

    /*@Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }*/


    //通过kafka来把消息发送到对应的topic的消息队列
    @Override
    public void sender(MySqlRowData rowData) {
        kafkaTemplate.send(
                topic, JSON.toJSONString(rowData)
        );
    }



    //kafka的topic监听器 发送出去后就会被这个监听器来监听
    @KafkaListener(topics = {"ad-search-mysql-data"}, groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?, ?> record){
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if(kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            MySqlRowData rowData = JSON.parseObject(
                    message.toString(),
                    MySqlRowData.class
            );
            System.out.println("kafka processMysqlRowData: " +
                    JSON.toJSONString(rowData));
        }
    }
}
