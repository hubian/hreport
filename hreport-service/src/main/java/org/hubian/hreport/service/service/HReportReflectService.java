package org.hubian.hreport.service.service;


import org.hubian.hreport.client.model.HReportTable;
import org.hubian.hreport.service.model.generated.ReportData;

import java.util.List;

/**
 * Created by masen on 2017/1/16.
 */
public interface HReportReflectService<T extends HReportTable>{
    public List<T> reflectReportDataToReportTable(List<ReportData> reportData, Class<T> clazz)throws Exception;

}
