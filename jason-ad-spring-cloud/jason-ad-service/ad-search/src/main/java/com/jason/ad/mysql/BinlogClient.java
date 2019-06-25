package com.jason.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.jason.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BinlogClient {

    private BinaryLogClient client;

    @Autowired
    private BinlogConfig config;

    @Autowired
    AggregationListener listener;

    //什么时候开始监听呢?最好的就是程序启动的时候就开始监听
    public void connect(){
        new Thread(() -> {
            client = new BinaryLogClient(
                    config.getHost(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );
            if(!StringUtils.isEmpty(config.getBinlogName())
                    && !config.getPosition().equals(-1L)){
                client.setBinlogFilename(config.getBinlogName());
                client.setBinlogPosition(config.getPosition());
            }
            client.registerEventListener(listener);
            try{
                //System.out.println("connecting to mysql start");
                log.info("connecting to mysql start");
                client.connect();
                log.info("connected to mysql start successfully");
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
        }).start();
    }

    public void close(){
        try{
            client.disconnect();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
