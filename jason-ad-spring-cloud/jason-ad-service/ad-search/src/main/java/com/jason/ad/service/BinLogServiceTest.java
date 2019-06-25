package com.jason.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

//这里使用Binlog的这个是为了我们广告系统与投放系统解耦

/**
 * select table_schema, table_name, column_name, ordinal_position from information_schema.columns where table_schema = 'jason_ad_data' and table_name = 'ad_unit_keyword'
 -> ;
 +---------------+-----------------+-------------+------------------+
 | TABLE_SCHEMA  | TABLE_NAME      | COLUMN_NAME | ORDINAL_POSITION |
 +---------------+-----------------+-------------+------------------+
 | jason_ad_data | ad_unit_keyword | id          |                1 |
 | jason_ad_data | ad_unit_keyword | unit_id     |                2 |
 | jason_ad_data | ad_unit_keyword | keyword     |                3 |
 +---------------+-----------------+-------------+------------------+
 */
//    Write---------------
//    WriteRowsEventData{tableId=85, includedColumns={0, 1, 2}, rows=[
//    [10, 10, 宝马]
//]}
//    Update--------------
//    UpdateRowsEventData{tableId=85, includedColumnsBeforeUpdate={0, 1, 2},
// includedColumns={0, 1, 2}, rows=[
//        {before=[10, 10, 宝马], after=[10, 11, 宝马]}
//]}
//    Delete--------------
//    DeleteRowsEventData{tableId=85, includedColumns={0, 1, 2}, rows=[
//    [11, 10, 奔驰]
//]}

//insert into `ad_plan` (`user_id`, `plan_name`, `plan_status`, `start_date`, `end_date`, `create_time`, `update_time`)
// values
// (9, 'plan', 1, '2019-01-01 00:00:00', '2019-01-01 00:00:00', '2019-01-01 00:00:00', '2019-01-01 00:00:00');
//    Write---------------
//    WriteRowsEventData{tableId=70, includedColumns={0, 1, 2, 3, 4, 5, 6, 7}, rows=[
//    [12, 10, plan, 1, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019,
//      Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019]
//]}
public class BinLogServiceTest {
    public static void main(String[] args) throws Exception {
        BinaryLogClient client = new BinaryLogClient(
                "localhost",
                3306,
                "root",
                "JasonJason"
        );

       //  client.setBinlogFilename();
        // client.setBinlogPosition();
        client.registerEventListener(event -> {
            EventData data = event.getData();
            if(data == null){
                System.out.println("Event data is Null!");
                System.out.println("-----------------------------------------------------------------------");
            }
            else if(data instanceof UpdateRowsEventData){
                System.out.println("update----------");
                System.out.println(data.toString());
            }else if(data instanceof WriteRowsEventData){
                System.out.println("write----------");
                System.out.println(data.toString());
            }else if(data instanceof DeleteRowsEventData){
                System.out.println("delete----------");
                System.out.println(data.toString());
            } else{
                System.out.println(data.toString());
                System.out.println("-----------------------------------------------------------------------");
            }
        });
        client.connect();
    }
}
