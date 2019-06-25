package com.jason.ad.mysql.listener;

import com.jason.ad.mysql.dto.BinlogRowData;

public interface Ilistener {

    void register();//因为我们可以对不同的表进行监听,所以我们需要去注册不同的监听器

    void onEvent(BinlogRowData eventData);//因为我们已经将MYSQL里的binlog 转换成 BinlogRowData了

}
