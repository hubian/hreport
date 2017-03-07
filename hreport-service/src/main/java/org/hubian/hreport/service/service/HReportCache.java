package org.hubian.hreport.service.service;



import org.hubian.hreport.service.model.generated.ReportFieldSchema;
import org.hubian.hreport.service.model.generated.ReportTableSchema;

import java.util.List;

/**
 * Created by masen on 2017/1/16.
 */
public interface HReportCache {
    public Integer getTableIDByTableName(String tableName);

    public List<ReportFieldSchema> getAllFieldSchemaByTableName(String name);

    public List<ReportFieldSchema> getAllFieldSchemaByTableId(Integer tableId);

    public List<ReportFieldSchema>  getKeyFields(int tableId);

    public ReportFieldSchema getFieldSchemaByTableIdAndFieldName(Integer tableId, String field);

    public ReportTableSchema getTableSchemaByTableName(String tableName);

    public ReportTableSchema getTableSchemaByID(Integer tableId);
}
