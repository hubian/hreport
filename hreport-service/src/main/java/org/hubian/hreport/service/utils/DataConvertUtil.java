package org.hubian.hreport.service.utils;


import com.alibaba.fastjson.JSONObject;
import org.hubian.hreport.service.model.enums.HReportFieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by masen on 2017/1/16.
 */
public class DataConvertUtil {
    public static Object convertFieldToDBField(Object obj, HReportFieldType fieldType){
        if (fieldType.isMoney()){
            return obj==null?0l:new BigDecimal(obj.toString()).multiply(new BigDecimal(100)).longValue();
        }else if (fieldType.isDate()){
            return obj==null?0l:((Date)obj).getTime();
        }else if(fieldType.isNumber()){
            return obj==null?Long.valueOf(0):Long.valueOf(obj.toString());
        }else if (fieldType.isJSON()){
            return obj==null? "":JSONObject.toJSONString(obj);
        }
        return obj==null?"":obj;
    }

    public static Object convertDBFieldToField(Object obj,HReportFieldType fieldType) {
       // System.out.println(obj);
        if (fieldType== HReportFieldType.BIGDECIMAL){
            return new BigDecimal(obj.toString()).divide(new BigDecimal(100));
        }else if(fieldType== HReportFieldType.FLOAT){
            return new BigDecimal(obj.toString()).divide(new BigDecimal(100)).floatValue();
        }else if(fieldType== HReportFieldType.DATE){
            //System.out.println(obj);
            return new Date(Long.valueOf(obj.toString()));
        }else if(fieldType== HReportFieldType.INTEGER){
            return ((Long)obj).intValue();
        }else if(fieldType==HReportFieldType.JSON){
            return JSONObject.parseObject(obj.toString());
        }else{
            return obj;
        }
    }
}
