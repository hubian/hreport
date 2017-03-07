package org.hubian.hreport.client.query;

import lombok.Getter;
import org.hubian.hreport.client.enums.HCompareType;

/**
 * Created by masen on 2017/1/16.
 */
public class QueryTermClause {
    @Getter
    private String field;
    @Getter
    private Object value;
    @Getter
    private HCompareType compareType;
    public QueryTermClause(String field, HCompareType compareType, Object value){
        this.field=field;
        this.value=value;
        this.compareType=compareType;
    }
}
