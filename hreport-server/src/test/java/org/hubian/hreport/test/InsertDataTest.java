package org.hubian.hreport.test;

import com.alibaba.fastjson.JSONObject;
import org.hubian.hreport.client.model.HReporter;
import org.hubian.hreport.client.service.HReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@DirtiesContext
public class InsertDataTest {
    @Autowired
    private HReportService reportService;

    @Test
    public void testInsertNewData() throws Exception {
        TestBean t=new TestBean();
        t.setDataVersion(String.valueOf(System.currentTimeMillis()));
        t.setId(2l);
        t.setName("hello");
        t.setSex(1);
        t.setHeight(1.84f);
        t.setAccount(new BigDecimal(100.01));
        t.setTime(new Date());
        JSONObject json=new JSONObject();
        json.put("key",1);
        json.put("value","value");
        t.setJson1(json);

        reportService.saveTableData(t,new HReporter("test","127.0.0.1"));
    }
    @Test
    public void testUpdateData() throws Exception {
        TestBean t=new TestBean();
        t.setDataVersion(String.valueOf(System.currentTimeMillis()));
        t.setId(2l);
        t.setName("hello");
        t.setSex(2);
        t.setHeight(1.84f);
        t.setAccount(new BigDecimal(100.01));
        t.setTime(new Date());
        reportService.saveTableData(t,new HReporter("test","127.0.0.1"));
    }

}