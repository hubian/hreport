package org.hubian.hreport.service.service.impl;

import com.google.common.collect.Lists;
import org.hubian.hreport.service.model.generated.ReportFieldSchema;
import org.hubian.hreport.service.model.generated.ReportFieldSchemaExample;
import org.hubian.hreport.service.model.generated.ReportTableSchema;
import org.hubian.hreport.service.model.generated.ReportTableSchemaExample;
import org.hubian.hreport.service.model.mapper.ReportDataMapper;
import org.hubian.hreport.service.model.mapper.ReportFieldSchemaMapper;
import org.hubian.hreport.service.model.mapper.ReportTableSchemaMapper;
import org.hubian.hreport.service.service.HReportCache;
import org.springframework.util.CollectionUtils;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by masen on 2017/1/17.
 */
@Service
public class HReportCacheImpl implements HReportCache {
    @Resource
    private ReportTableSchemaMapper reportTableSchemaMapper;
    @Resource
    private ReportFieldSchemaMapper reportFieldSchemaMapper;
    @Resource
    private ReportDataMapper reportDataMapper;

    public Integer getTableIDByTableName(String tableName) {
        ReportTableSchemaExample example=new ReportTableSchemaExample();
        example.createCriteria().andTableNameEqualTo(tableName);
        List<ReportTableSchema> list=reportTableSchemaMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(list))
        return reportTableSchemaMapper.selectByExample(example).get(0).getTableId();
        return null;
    }

    public List<ReportFieldSchema> getAllFieldSchemaByTableName(String tableName) {
        Integer tableId=getTableIDByTableName(tableName);
        return getAllFieldSchemaByTableId(tableId);
    }

    public List<ReportFieldSchema> getAllFieldSchemaByTableId(Integer tableId) {
        ReportFieldSchemaExample example=new ReportFieldSchemaExample();
        example.createCriteria().andTableIdEqualTo(tableId);
        return reportFieldSchemaMapper.selectByExample(example);
    }

    public List<ReportFieldSchema> getKeyFields(int tableId){
        ReportFieldSchemaExample example=new ReportFieldSchemaExample();
        example.createCriteria().andTableIdEqualTo(tableId);
        List<ReportFieldSchema> fieldSchemas= reportFieldSchemaMapper.selectByExample(example);
        List<ReportFieldSchema> keyFields= Lists.newArrayList();
        for (ReportFieldSchema f:fieldSchemas){
            if (f.getPrimaryKey()){
                keyFields.add(f);
            }
        }
        return keyFields;
    }

    public ReportFieldSchema getFieldSchemaByTableIdAndFieldName(Integer tableId, String field) {
        ReportFieldSchemaExample example=new ReportFieldSchemaExample();
        example.createCriteria().andTableIdEqualTo(tableId).andFieldNameEqualTo(field);
        List<ReportFieldSchema> list=reportFieldSchemaMapper.selectByExample(example);
        return CollectionUtils.isEmpty(list)?null:list.get(0);

    }

    public ReportTableSchema getTableSchemaByTableName(String tableName) {
        ReportTableSchemaExample reportTableSchemaExample=new ReportTableSchemaExample();
        reportTableSchemaExample.createCriteria().andTableNameEqualTo(tableName);
        List<ReportTableSchema> list=reportTableSchemaMapper.selectByExample(reportTableSchemaExample);
        return CollectionUtils.isEmpty(list)?null:list.get(0);
    }

    public ReportTableSchema getTableSchemaByID(Integer tableId) {
        ReportTableSchemaExample reportTableSchemaExample=new ReportTableSchemaExample();
        reportTableSchemaExample.createCriteria().andTableIdEqualTo(tableId);
        List<ReportTableSchema> list=reportTableSchemaMapper.selectByExample(reportTableSchemaExample);
        return CollectionUtils.isEmpty(list)?null:list.get(0);
    }
}
