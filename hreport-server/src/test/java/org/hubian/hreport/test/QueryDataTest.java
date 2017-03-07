package org.hubian.hreport.test;



import org.hubian.hreport.client.model.HReporter;
import org.hubian.hreport.client.query.HReportQuery;
import org.hubian.hreport.client.service.HReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@DirtiesContext
public class QueryDataTest {
    @Autowired
    private HReportService reportService;



    public void testQuery1() throws Exception {
        HReportQuery query=new HReportQuery(TestBean.class);
        query.equalsTo("id",2l);
        List<TestBean> list= reportService.queryTableData(query,new HReporter("test","127.0.0.1"));
        System.out.println(list==null?"no found":list.toString());
    }



    public void testQuery2() throws Exception {
        HReportQuery query=new HReportQuery(TestBean.class);
        query.greaterThan("height",Float.valueOf(1.65f))
                .lessThanOrEqualTo("height",Float.valueOf(1.85f));
        List<TestBean> list= reportService.queryTableData(query,new HReporter("test","127.0.0.1"));
        System.out.println(list==null?"no found":list.toString());
    }

    @Test
    public void queryLasted() throws Exception {
        List<TestBean> list= reportService.queryLastedData(TestBean.class);
        System.out.println(list==null?"no found":list.toString());
    }
}