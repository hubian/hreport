package org.hubian.hreport.client.annotation;

import java.lang.annotation.*;

/**
 * Created by masen on 2017/1/12.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HReportFieldAna {
    boolean isKey() default false;
}