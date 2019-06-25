package com.jason.ad.utils;

import com.jason.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class CommonUtils {
    public static String md5(String value){
        return DigestUtils.md2Hex(value).toUpperCase();
    }



    /*
    把字符串转化成date类型
     */
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd", "yyyy:MM:dd"
            //符合任意一个pattern都可以
    };
    public static Date parseStringDate(String dateString)
            throws AdException{
        try{
            return DateUtils.parseDate(
                    dateString, parsePatterns
            );
        } catch (Exception ex){
            throw new AdException(ex.getMessage());
        }
    }

}


