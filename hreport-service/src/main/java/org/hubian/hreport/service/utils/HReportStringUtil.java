package org.hubian.hreport.service.utils;

/**
 * Created by masen on 2017/1/16.
 */
public class HReportStringUtil {
    public static String firstUpper(String str){
        return str.replaceFirst(str.substring(0, 1),str.substring(0, 1).toUpperCase())  ;
    }
}
