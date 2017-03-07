package org.hubian.hreport.service.service;



import org.hubian.hreport.client.model.HReportTable;
import org.hubian.hreport.client.model.HReporter;
import org.hubian.hreport.client.query.HReportQuery;
import org.hubian.hreport.service.model.generated.ReportData;
import org.hubian.hreport.service.model.generated.ReportTableSchema;

import java.util.List;

/**
 * Created by masen on 2017/1/16.
 */
public interface HReportDataService<T extends HReportTable> {
    public List<ReportData> queryReportData(HReportQuery query)throws Exception;
    public void deleteDataByTableId(Integer tableId) throws  Exception;
    public void deleteOldData(HReportTable tableData, Integer tableId) throws Exception;
    public void insertTableSchema(HReportTable tableData, HReporter lepayReporter)throws Exception;
    public void updateTableSchema(HReportTable tableData, ReportTableSchema tableSchema, HReporter lepayReporter) throws Exception;
    public void insertData(HReportTable tableData, Integer tableId) throws Exception;
    public List<ReportData> queryLastedReportData(Class<T> _class)throws Exception;
}
