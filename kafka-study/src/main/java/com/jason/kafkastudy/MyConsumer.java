package com.jason.kafkastudy;


import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class MyConsumer {

    private static KafkaConsumer<String, String> consumer;

    private static Properties properties;

    static {
        properties = new Properties();
        properties.put("bootstrap.servers", "127.0.0.1:9092");//host和point, kafka启动的默认端口
        properties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");//类的名称,制定了用来反序列化key的类
        properties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");//制定了用来反序列化消息内容的类
        properties.put("group.id", "KafkaStudy");
    }


    //Consumer消费消息的方式: 自动提交位移
    private static void generalConsumeMessageAutoCommit(){
        properties.put("enable.auto.commit", true);//允许自动提交位移 每隔5秒提交一次位移
        consumer = new KafkaConsumer<>(properties);
        //订阅topic 可以是一个list of topics
        consumer.subscribe(Collections.singleton("jason-kafka-study-x"));

        //拉取循环过程 提取kafka中的消息
        try{
            while(true){
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);//100是超时时间

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(), record.key(),
                            record.value()
                    ));

                    if(record.value().equals("done")) {
                        flag = false;
                    }
                }

                if(!flag){
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    //Consumer消费消息的方式: 手动同步提交当前位移
    private static void generalConsumeMessageSyncCommit(){
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);

        //订阅topic 可以是一个topic或者list of topics
        consumer.subscribe(Collections.singleton("jason-kafka-study-x"));

        while(true){
            boolean flag = true;

            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(), record.key(),
                        record.value()
                ));
                if(record.value().equals("done")){
                    flag = false;
                }
            }

            try{
                consumer.commitSync(); //有可能会发生阻塞 可以多次for循环之后再来commit
                // 但这样也会有消息重复的问题 => 使用异步提交
            } catch (CommitFailedException ex){
                System.out.println("commit failed error: " + ex.getMessage());
            }

            if(!flag){
                break;
            }
        }
    }

    //Consumer消费消息的方式: 手动异步提交当前位移

    private static void generalConsumeMessageAsyncCommit(){
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singleton("jason-kafka-study-x"));

        while(true){
            boolean flag = true;

            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(), record.key(),
                        record.value()
                ));
                if(record.value().equals("done")){
                    flag = false;
                }
            }

            consumer.commitAsync();
            // 异步提交 不会发生阻塞 速度快
            // 但会有一个问题 就是异步提交失败, 服务器不会返回进行重试
            // 而同步提交会重试直到抛出异常或者提交成功
            // 因为在异步提交的情况下进行重试 有可能会导致位移覆盖
            // 比如A应该放在位移offset = 2000 位置,但commit 失败了
            // 此时有B进来 提交成功commit 放在了位移offset = 3000 位置
            // 如果此时A进行重试,加入成功 那么就会将已经成功提交的位移又从3000 回滚到了2000
            // 那么就会导致消息的重复消费
            // =>>>>下一种方法 带回调!
            if(!flag){
                break;
            }
        }
    }

    //Consumer消费消息的方式: 手动异步提交当前位移带回调
    private static void generalConsumeMessageAsyncCommitWithCallback(){
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singleton("jason-kafka-study-x"));

        while(true){
            boolean flag = true;

            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format(
                        "topic = %s, partition = %s, key = %s, value = %s",
                        record.topic(), record.partition(), record.key(),
                        record.value()
                ));
                if(record.value().equals("done")){
                    flag = false;
                }
            }


            // java 8 接口式的函数
            // 这里spring内部已经封装好了一个方法: 设置了一个全局单调递增的id,
            // 如果发生异常的时候,在发生异常的那个线程里面的id和全局的id进行比对,如果是一样的 就可以重新提交
            // 如果不一致, 那说明已经有新的信息被commit了,那么此时就会放弃重新提交
            consumer.commitAsync((map, e) -> {
                //如果异步提交如果失败了 那个异常信息不会被nullify 而会被记录保留传进来
                if(e != null){
                    System.out.println("commit fail for offsets: " + e.getMessage());
                    System.out.println("<TopicPartition, OffsetAndMetadata> map info: " +
                        map.toString());
                }
            });

            if(!flag){
                break;
            }
        }
    }

    //Consumer消费消息的方式: 混合同步与异步提交位移 更为普遍的方式
    private static void mixSyncAndAsyncCommit(){
        properties.put("auto.commit.offset", false);
        consumer = new KafkaConsumer<>(properties);

        consumer.subscribe(Collections.singleton("jason-kafka-study-x"));

        try{
            while(true){
                boolean flag = true;
                ConsumerRecords<String, String> records =
                        consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(String.format(
                            "topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(), record.key(),
                            record.value()
                    ));
                    if(record.value().equals("done")){
                        flag = false;
                    }
                }
                if(!flag) {
                    break;
                }

                consumer.commitAsync();
            }
        } catch (Exception ex){
            System.out.println("commit async error:" + ex.getMessage());
        } finally {
            // 如果异步提交失败了 会抛出异常 这里捕获异常
            // 那么我们这里有一个同步提交 尽最大可能的保证提交成功
            try{
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    public static void main(String[] args) {
        generalConsumeMessageAutoCommit();
    }
}
