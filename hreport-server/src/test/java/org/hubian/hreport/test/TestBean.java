package org.hubian.hreport.test;



import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.hubian.hreport.client.annotation.HReportFieldAna;
import org.hubian.hreport.client.annotation.HReportTableAna;
import org.hubian.hreport.client.model.HReportTable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by masen on 2017/1/13.
 */
@Data
@HReportTableAna(deleteOldDataIfChanged = true)
public class TestBean extends HReportTable {
        @HReportFieldAna(isKey = true)
        private Long id;
        @HReportFieldAna(isKey = false)
        private String name;
        private Integer sex;
        private BigDecimal account;
        private Float height;
        private Date time;
        private JSONObject json1;
        private JSONObject json2;
}
