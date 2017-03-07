package org.hubian.hreport.service.service.impl;



import org.hubian.hreport.client.annotation.HReportTableAna;
import org.hubian.hreport.client.model.HReportTable;
import org.hubian.hreport.client.model.HReporter;
import org.hubian.hreport.client.query.HReportQuery;
import org.hubian.hreport.client.service.HReportService;
import org.hubian.hreport.service.model.generated.ReportData;
import org.hubian.hreport.service.model.generated.ReportTableSchema;
import org.hubian.hreport.service.service.HReportCache;
import org.hubian.hreport.service.service.HReportDataService;
import org.hubian.hreport.service.service.HReportReflectService;
import org.hubian.hreport.service.utils.ClassSignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by masen on 2017/1/12.
 */
@Service("jReportDataService")
public class HReportServiceImpl<T extends HReportTable> implements HReportService<T> {
    private static final Logger logger = LoggerFactory.getLogger(HReportServiceImpl.class);

    @Resource
    private HReportDataService dataService;
    @Resource
    private HReportReflectService dataReflectService;
    @Resource
    private HReportCache reportCache;

    @Transactional(rollbackFor = Exception.class)
    public void saveTableData(HReportTable table, HReporter reporter) throws Exception  {
        String tableName=table.getClass().getName();
        ReportTableSchema tableSchema=reportCache.getTableSchemaByTableName(tableName);
        boolean isNew=false;
        if (tableSchema==null){//insert new table schema
            isNew=true;
            dataService.insertTableSchema(table, reporter);
        }else{
            String newSignCode= ClassSignUtil.signFields(table.getClass());
            if (!tableSchema.getSignCode().equalsIgnoreCase(newSignCode)){//update table schema
                isNew=true;
                dataService.updateTableSchema(table,tableSchema, reporter);
                HReportTableAna annotationSchema=table.getClass().getAnnotation(HReportTableAna.class);
                if (annotationSchema==null||annotationSchema.deleteOldDataIfChanged()){
                    dataService.deleteDataByTableId(tableSchema.getTableId());
                }
            }
        }
        Integer tableId=reportCache.getTableIDByTableName(tableName);
        if (!isNew){
            dataService.deleteOldData(table,tableId);
        }
        logger.debug("try save:{}",table);
        dataService.insertData(table,tableId);
    }

    public List<T> queryLastedData(Class<T> _class) throws Exception {
        List<ReportData> reportDatas= dataService.queryLastedReportData(_class);
        List<T> res=dataReflectService.reflectReportDataToReportTable(reportDatas,_class);
        return res;
    }

    public List<T> queryTableData(HReportQuery query, HReporter reporter) throws Exception {
        List<ReportData> reportDatas=dataService.queryReportData(query);
         List<T> res=dataReflectService.reflectReportDataToReportTable(reportDatas,query.getClazz());
        return res;
    }
}
