package com.jason.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public class CommonUtils {

    public static <K, V> V getorCreate(K key, Map<K, V> map,
                                     Supplier<V> factory){
        //我们传进来一个key和一个map 如果在这个map里面这个key不存在呢,
        // 我们通过这个传进来的supplier factory去返回一个新的对象
        return map.computeIfAbsent(key, k->factory.get());
    }

    public static String stringConcat(String... args){
        StringBuilder result = new StringBuilder();
        for(String arg : args){
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    //解析我们BinLog日志里面的那个日期的string
    //Tue Jan 01 08:00:00 CST 2019
    //这里我们其实是输入的 08:00:00
    public static Date parseStringDate(String dateString){
        try{
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US
            );
            return DateUtils.addHours(
                    dateFormat.parse(dateString),
                    -8);//北京时 间是-8, 纽约时间是+4

        } catch (ParseException ex){
            log.error("parseStringDate Error: {}", dateString);
            return null;
        }
    }
}
