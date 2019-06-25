package com.jason.kafkastudy;

//自定义的partitioner

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {

    @Override
    //keyBytes => key的bytes表示
    //cluster kafka集群的信息
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes,
                         Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int numPartitions = partitionInfos.size();//Partition的个数

        //必须要传递key

        if (null == keyBytes || !(key instanceof String)) {
            throw new InvalidRecordException("kafka message must have a string key");
        }

        if (numPartitions == 1){
            return 0;
        }
        if(key.equals("name")){
            return numPartitions - 1;//发送到最后一个分区
        }
        //Utils.murmur2(keyBytes) 对key进行取hash值
        return Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
