package org.hubian.hreport.client.model;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
/**
 * Created by masen on 2017/1/12.
 *
 *  support Number:Integer/Long
            Money:Float/BigDecimal
            String
            Date
            JSON
 */
@Data
public class HReportTable {
    //the lasted data version
    private String dataVersion=String.valueOf(System.currentTimeMillis());
    public String toString(){return JSONObject.toJSONString(this);}
}
