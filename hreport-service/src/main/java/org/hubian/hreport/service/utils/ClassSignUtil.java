package org.hubian.hreport.service.utils;

import org.hubian.hreport.client.annotation.HReportFieldAna;

import java.lang.reflect.Field;

/**
 * Created by masen on 2017/1/18.
 */
public class ClassSignUtil {
    public static String signFields(Class clazz){
        Field[] fields=clazz.getDeclaredFields();
        if (fields.length==0){
            return null;
        }
        String origin="";
        for (Field field:fields){

            origin+=field.getType().getName()+"--"+field.getName();
            HReportFieldAna hReportFieldAna=field.getAnnotation(HReportFieldAna.class);
            if (hReportFieldAna!=null){
                origin+="-"+hReportFieldAna.isKey();
            }
        }
        return Md5Encrypt.md5(origin);
    }
    class TestA{
        private int m;
    }
    class TestB{
        private int m;
        private int n;
    }
    class TestC{
        private int m;
    }

    public static void main(String[] args){
        System.out.println(ClassSignUtil.signFields(TestA.class));
        System.out.println(ClassSignUtil.signFields(TestA.class).equalsIgnoreCase(
                ClassSignUtil.signFields(TestB.class)
        ));
        System.out.println(ClassSignUtil.signFields(TestA.class).equalsIgnoreCase(
                ClassSignUtil.signFields(TestC.class)
        ));
    }
}
