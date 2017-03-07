package org.hubian.hreport.service.service.impl;




import org.hubian.hreport.client.annotation.HReportFieldAna;
import org.hubian.hreport.client.enums.HCompareType;
import org.hubian.hreport.client.model.HReportTable;
import org.hubian.hreport.client.model.HReporter;
import org.hubian.hreport.client.query.HReportQuery;
import org.hubian.hreport.client.query.QueryTermClause;
import org.hubian.hreport.service.model.enums.HReportFieldType;
import org.hubian.hreport.service.model.generated.*;
import org.hubian.hreport.service.model.mapper.ReportDataMapper;
import org.hubian.hreport.service.model.mapper.ReportFieldSchemaMapper;
import org.hubian.hreport.service.model.mapper.ReportTableSchemaMapper;
import org.hubian.hreport.service.service.HReportCache;
import org.hubian.hreport.service.service.HReportDataService;
import org.hubian.hreport.service.utils.ClassSignUtil;
import org.hubian.hreport.service.utils.DataConvertUtil;
import org.hubian.hreport.service.utils.HReportStringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 * Created by masen on 2017/1/16.
 */
@Service
public class HReportDataServiceImpl<T extends HReportTable> implements HReportDataService<T> {
    @Resource
    private ReportTableSchemaMapper reportTableSchemaMapper;
    @Resource
    private ReportFieldSchemaMapper reportFieldSchemaMapper;
    @Resource
    private ReportDataMapper reportDataMapper;
    @Resource
    private HReportCache reportCache;

    public List<ReportData> queryReportData(HReportQuery query)throws Exception{
        ReportDataExample example = new ReportDataExample();
        ReportDataExample.Criteria criteria = example.createCriteria();
        Integer tableId=reportCache.getTableIDByTableName(query.getReportName());
        if (tableId==null){return null;}
        criteria.andTableIdEqualTo(tableId);
        if (!CollectionUtils.isEmpty(query.getQueryClauses())){
            for(QueryTermClause c:query.getQueryClauses()){
                ReportFieldSchema fieldSchema=reportCache.getFieldSchemaByTableIdAndFieldName(tableId,c.getField());
                HReportFieldType fieldType= HReportFieldType.getByValue(fieldSchema.getFieldType());
                Object dbFiledData= DataConvertUtil.convertFieldToDBField(c.getValue(),fieldType);
                Method method = null;
                if (c.getCompareType()== HCompareType.EqualTo){
                    method = criteria.getClass().getMethod("and" + HReportStringUtil.firstUpper(fieldSchema.getDataField()) + "EqualTo", fieldType.getDbClass());
                }else if (c.getCompareType()== HCompareType.LessThan){
                    method = criteria.getClass().getMethod("and" + HReportStringUtil.firstUpper(fieldSchema.getDataField()) + "LessThan", fieldType.getDbClass());
                }else if (c.getCompareType()== HCompareType.GreaterThan){
                    method = criteria.getClass().getMethod("and" + HReportStringUtil.firstUpper(fieldSchema.getDataField()) + "GreaterThan", fieldType.getDbClass());
                }else if (c.getCompareType()== HCompareType.LessThanOrEqualTo){
                    method = criteria.getClass().getMethod("and" + HReportStringUtil.firstUpper(fieldSchema.getDataField()) + "LessThanOrEqualTo", fieldType.getDbClass());
                }else if (c.getCompareType()== HCompareType.GreaterThanOrEqualTo) {
                    method = criteria.getClass().getMethod("and" + HReportStringUtil.firstUpper(fieldSchema.getDataField()) + "GreaterThanOrEqualTo", fieldType.getDbClass());
                }else if (c.getCompareType()== HCompareType.NotEqualTo) {
                    method = criteria.getClass().getMethod("and" + HReportStringUtil.firstUpper(fieldSchema.getDataField()) + "NotEqualTo", fieldType.getDbClass());
                }
                if (method==null){
                    throw new Exception("un support compare type");
                }
                method.invoke(criteria,dbFiledData);
            }
        }
        return reportDataMapper.selectByExample(example);
    }

    public void deleteDataByTableId(Integer tableId) throws Exception {
        ReportDataExample example=new ReportDataExample();
        example.createCriteria().andTableIdEqualTo(tableId);
        reportDataMapper.deleteByExample(example);
    }

    public void deleteOldData(HReportTable tableData, Integer tableId) throws Exception{
        ReportDataExample example = new ReportDataExample();
        ReportDataExample.Criteria criteria = example.createCriteria();
        criteria.andTableIdEqualTo(tableId);
        for (ReportFieldSchema keyField : reportCache.getKeyFields(tableId)) {
            Method valueMethod=tableData.getClass().getMethod("get"+ HReportStringUtil.firstUpper(keyField.getFieldName()));
            HReportFieldType fieldType= HReportFieldType.getByValue(keyField.getFieldType());
            Object value= DataConvertUtil.convertFieldToDBField(valueMethod.invoke(tableData),fieldType);
            Method method = criteria.getClass().getMethod("and" + HReportStringUtil.firstUpper(keyField.getDataField()) + "EqualTo", fieldType.getDbClass());
            method.invoke(criteria, value);
        }
        reportDataMapper.deleteByExample(example);
    }


    public void insertTableSchema(HReportTable tableData, HReporter lepayReporter)throws Exception{
        Class clazz=tableData.getClass();
        ReportTableSchema tableSchema=new ReportTableSchema();
        tableSchema.setSignCode(ClassSignUtil.signFields(clazz));
        tableSchema.setTableName(clazz.getName());
        tableSchema.setCreateDate(new Date());
        tableSchema.setCreateIp(lepayReporter.getIp());
        tableSchema.setUpdateDate(new Date());
        tableSchema.setUpdateIp(lepayReporter.getIp());
        tableSchema.setLastedVersion(tableData.getDataVersion());
        if(reportTableSchemaMapper.insert(tableSchema)<=0){
            throw new Exception("save Report Table Schema failed");
        }
        insertFieldSchema(clazz,tableSchema.getTableId());
    }

    private void insertFieldSchema(Class clazz,Integer tableId) throws  Exception{
        int numberField=0;
        int stringField=0;
        int jsonField=0;
        for(Field field:clazz.getDeclaredFields()){
            HReportFieldType fieldType= HReportFieldType.getByType(field.getType());
            if (fieldType.isUnsupport()){
                throw new Exception("un-support field type:"+fieldType.getDesc()+field.getType());
            }
            ReportFieldSchema fieldSchema=new ReportFieldSchema();
            fieldSchema.setFieldName(field.getName());
            fieldSchema.setTableId(tableId);
            fieldSchema.setFieldType(fieldType.getValue());
            if (fieldType.getDbClass()==Long.class){
                numberField++;
                fieldSchema.setDataField("lf"+numberField);
            }else if (fieldType.isJSON()){
                jsonField++;
                fieldSchema.setDataField("jf"+jsonField);
            }else{
                stringField++;
                fieldSchema.setDataField("sf"+stringField);
            }
            HReportFieldAna annotationSchema=field.getAnnotation(HReportFieldAna.class);
            fieldSchema.setPrimaryKey(false);
            if (annotationSchema!=null){
                fieldSchema.setPrimaryKey(annotationSchema.isKey());
            }
            if(reportFieldSchemaMapper.insert(fieldSchema)<=0){
                throw new Exception("save Report Field failed.");
            }
        }
    }

    public void updateTableSchema(HReportTable tableData, ReportTableSchema tableSchema, HReporter lepayReporter) throws Exception{
        tableSchema.setSignCode(ClassSignUtil.signFields(tableData.getClass()));
        tableSchema.setTableName(tableData.getClass().getName());
        tableSchema.setUpdateDate(new Date());
        tableSchema.setUpdateIp(lepayReporter.getIp());
        tableSchema.setLastedVersion(tableData.getDataVersion());
        if(reportTableSchemaMapper.updateByPrimaryKey(tableSchema)<=0){
            throw new Exception("update Report Table Schema failed:tableId="+tableSchema.getTableId());
        }
        ReportFieldSchemaExample schemaExample=new ReportFieldSchemaExample();
        schemaExample.createCriteria().andTableIdEqualTo(tableSchema.getTableId());
        if(reportFieldSchemaMapper.deleteByExample(schemaExample)<=0){
            throw new Exception("delete schema fileds failed:tableId="+tableSchema.getTableId());
        }
        insertFieldSchema(tableData.getClass(),tableSchema.getTableId());
    }



    public void insertData(HReportTable tableData, Integer tableId) throws Exception{
        ReportData reportData=new ReportData();
        reportData.setTableId(tableId);
        reportData.setDataVersion(tableData.getDataVersion());
        for (ReportFieldSchema keyField : reportCache.getAllFieldSchemaByTableId(tableId)) {
            HReportFieldType fieldType= HReportFieldType.getByValue(keyField.getFieldType());
            Method valueMethod=tableData.getClass().getMethod("get"+ HReportStringUtil.firstUpper(keyField.getFieldName()));
            Object value=DataConvertUtil.convertFieldToDBField(valueMethod.invoke(tableData),fieldType);
            Method method = reportData.getClass().getMethod("set" + HReportStringUtil.firstUpper(keyField.getDataField()), fieldType.getDbClass());
            method.invoke(reportData, value);
        }
        if (reportDataMapper.insert(reportData)<=0){
            throw new Exception("save table data failed");
        }
        ReportTableSchema tableSchema=  reportCache.getTableSchemaByID(tableId);
        if (tableSchema==null){
            throw new Exception("table not exist");
        }
        tableSchema.setLastedVersion(tableData.getDataVersion());
        if(reportTableSchemaMapper.updateByPrimaryKey(tableSchema)<0){
            throw new Exception("update table schema failed:tableId="+tableSchema.getTableId());
        }
    }

    public List<ReportData> queryLastedReportData(Class<T> _class) throws Exception {
        ReportTableSchema table=reportCache.getTableSchemaByTableName(_class.getName());
        ReportDataExample example = new ReportDataExample();
        ReportDataExample.Criteria criteria = example.createCriteria();
        criteria.andTableIdEqualTo(table.getTableId()).andDataVersionEqualTo(table.getLastedVersion());
        return reportDataMapper.selectByExample(example);
    }
}
