package com.jason.ad.runner;

import com.jason.ad.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//在程序启动的时候需要run的
@Slf4j
@Component
public class BinlogRunner implements CommandLineRunner{
    @Autowired
    private BinlogClient client;

    @Override
    public void run(String... args) throws Exception {
        log.info("Coming in BinlogRunner...");
        client.connect();
    }
}
