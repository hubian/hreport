package org.hubian.hreport.service.service.impl;

import com.google.common.collect.Lists;

import org.hubian.hreport.client.model.HReportTable;
import org.hubian.hreport.service.model.enums.HReportFieldType;
import org.hubian.hreport.service.model.generated.ReportData;
import org.hubian.hreport.service.model.generated.ReportFieldSchema;
import org.hubian.hreport.service.service.HReportCache;
import org.hubian.hreport.service.service.HReportReflectService;
import org.hubian.hreport.service.utils.DataConvertUtil;
import org.hubian.hreport.service.utils.HReportStringUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by masen on 2017/1/16.
 */
@Service
public class HReportReflectServiceImpl<T extends HReportTable> implements HReportReflectService<T> {
    @Resource
    private HReportCache reportCache;

    public List<T> reflectReportDataToReportTable(List<ReportData> reportData, Class<T> clazz) throws Exception {
        if (CollectionUtils.isEmpty(reportData)) {
            return null;
        }
        List<T> list = Lists.newArrayList();
        List<ReportFieldSchema> fieldSchemas = reportCache.getAllFieldSchemaByTableName(clazz.getName());
        for (ReportData data : reportData) {
            T t = clazz.newInstance();
            for (ReportFieldSchema f : fieldSchemas) {
                HReportFieldType fieldType = HReportFieldType.getByValue(f.getFieldType());
                Method dataMethod = ReportData.class.getMethod("get" + HReportStringUtil.firstUpper(f.getDataField()));
                Object value = DataConvertUtil.convertDBFieldToField(dataMethod.invoke(data), fieldType);
                try {
                    Method setMethod = clazz.getMethod("set" + HReportStringUtil.firstUpper(f.getFieldName()),
                            fieldType.getClazz()
                    );
                    setMethod.invoke(t, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;//容忍出错
                }
            }
            list.add(t);
        }
        return list;
    }
}
