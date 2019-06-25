package com.jason.ad.sender;

import com.jason.ad.mysql.dto.MySqlRowData;

//投递增量数据的接口
public interface ISender {

    void sender(MySqlRowData rowData);
}
