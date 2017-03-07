package org.hubian.hreport.client.service;

import org.hubian.hreport.client.model.HReportTable;
import org.hubian.hreport.client.model.HReporter;
import org.hubian.hreport.client.query.HReportQuery;

import java.util.List;

/**
 * Created by masen on 2017/1/12.
 */
public interface HReportService<T extends HReportTable>{
    public List<T> queryLastedData(Class<T> _class)throws Exception;
    public void saveTableData(T table, HReporter reporter)throws Exception ;
    public List<T> queryTableData(HReportQuery query, HReporter reporter)throws Exception;
}
