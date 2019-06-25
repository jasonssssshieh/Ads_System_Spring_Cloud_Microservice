package com.jason.kafkastudy;


import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class MyProducer {

    private static KafkaProducer<String, String> producer;

    static {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "127.0.0.1:9092");//host和point, kafka启动的默认端口
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");//类的名称,制定了用来序列化key的类
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");//制定了用来序列化消息内容的类

        properties.put("partitioner.class",
                "com.jason.kafkastudy.CustomPartitioner");//自定义的分配器
        producer = new KafkaProducer<>(properties);
    }

    //第一种发送消息方法: 只管发送 不管发送结果 在consumer里面只会有value 而不会有key
    private static void sendMessageForgetResult(){
        ProducerRecord<String, String> record = new ProducerRecord<>(
                "jason-kafka-study",
                "name",
                "ForgetResult"
        );
        producer.send(record);
        producer.close();
    }

    //第二种: 同步发送消息 同步召唤
    private static void sendMessageSync() throws Exception {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                "jason-kafka-study",
                "name",
                "sync"
        );
        RecordMetadata result = producer.send(record).get();//返回一个feature对象

        System.out.println(result.topic());
        System.out.println(result.partition());
        System.out.println(result.offset());

        producer.close();
    }

    //第三种 异步发送: 需要一个异步回调类, 异步回调类需要实现korable接口, 这个接口只有一个onCompletion的方法
    private static class MyProducerCallBack implements Callback{

        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {

            if( e != null){
                e.printStackTrace();
                return;
            }

            System.out.println(recordMetadata.topic());
            System.out.println(recordMetadata.partition());
            System.out.println(recordMetadata.offset());
            System.out.println("Coming in MyProducerCallBack");
        }
    }

    private static void sendMessageCallback(){
        ProducerRecord<String, String> record = new ProducerRecord<>(
                "jason-kafka-study-x",
                "name",
                "callback1"
        );
        producer.send(record, new MyProducerCallBack());
        record = new ProducerRecord<>(
                "jason-kafka-study-x",
                "name-x",
                "callback2"
        );
        producer.send(record, new MyProducerCallBack());
        record = new ProducerRecord<>(
                "jason-kafka-study-x",
                "name-y",
                "callback3"
        );
        producer.send(record, new MyProducerCallBack());
        record = new ProducerRecord<>(
                "jason-kafka-study-x",
                "name-z",
                "callback4"
        );
        producer.send(record, new MyProducerCallBack());
        producer.close();
    }

    public static void main(String[] args) throws Exception {
        //sendMessageForgetResult();
        //sendMessageSync();
        sendMessageCallback();
    }
}
