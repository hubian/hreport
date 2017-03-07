package org.hubian.hreport.client.annotation;

import java.lang.annotation.*;

/**
 * Created by masen on 2017/1/12.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HReportTableAna {
    boolean deleteOldDataIfChanged() default true;
}