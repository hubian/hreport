package org.hubian.hreport.client.query;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.hubian.hreport.client.enums.HCompareType;

import java.util.List;

/**
 * Created by masen on 2017/1/16.
 */
public class HReportQuery {
    @Getter
    private List<QueryTermClause> queryClauses;
    @Getter
    private Class clazz;
    public HReportQuery(Class clazz){
        this.clazz=clazz;
        queryClauses= Lists.newArrayList();
    }
    public String getReportName(){
        return clazz.getName();
    }
    public HReportQuery equalsTo(String name, Object value){
        queryClauses.add(new QueryTermClause(name, HCompareType.EqualTo, value));
        return this;
    }
    public HReportQuery notEqualsTo(String name, Object value){
        queryClauses.add(new QueryTermClause(name, HCompareType.NotEqualTo, value));
        return this;
    }
    public HReportQuery greaterThan(String name, Object value){
        queryClauses.add(new QueryTermClause(name, HCompareType.GreaterThan,value));
        return this;
    }
    public HReportQuery lessThan(String name, Object value){
        queryClauses.add(new QueryTermClause(name, HCompareType.LessThan,value));
        return this;
    }
    public HReportQuery greaterThanOrEqualTo(String name, Object value){
        queryClauses.add(new QueryTermClause(name, HCompareType.GreaterThanOrEqualTo,value));
        return this;
    }
    public HReportQuery lessThanOrEqualTo(String name, Object value){
        queryClauses.add(new QueryTermClause(name, HCompareType.LessThanOrEqualTo,value));
        return this;
    }
}
