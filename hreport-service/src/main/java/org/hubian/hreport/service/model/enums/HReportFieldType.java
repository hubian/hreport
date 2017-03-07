package org.hubian.hreport.service.model.enums;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public enum HReportFieldType {
	UNSUPPORT(0,"Supported",Object.class,Object.class),
	STRING(1, "String",String.class,String.class),
	INTEGER(2,"Integer",Integer.class,Long.class),
	LONG(3,"Long",Long.class,Long.class),
	FLOAT(4,"Float",Float.class,Long.class),
	BIGDECIMAL(5,"BigDecimal",BigDecimal.class,Long.class),
	DATE(6,"Date",Date.class,Long.class),
	JSON(7,"JSON",JSONObject.class,String.class);

	@Getter
	private int value;
	@Getter
	private String desc;
	@Getter
	private Class clazz;
	@Getter
	private Class dbClass;
	HReportFieldType(int value, String desc, Class clazz, Class dbClass) {
		this.value = value;
		this.desc = desc;
		this.clazz=clazz;
		this.dbClass=dbClass;
	}
	private static Map<Integer, HReportFieldType> valueMap = new HashMap<Integer, HReportFieldType>();
	static {
		for (HReportFieldType type : HReportFieldType.values())
			valueMap.put(type.getValue(), type);
	}
	public static HReportFieldType getByValue(int value) {
		return valueMap.get(value);
	}

	public static HReportFieldType getByType(Class _class){
		if(_class.getName().contains("String")){
			return HReportFieldType.STRING;
		} else if(_class.getName().equals("int")||_class.getName().contains("Integer")){
			return HReportFieldType.INTEGER;
		}else if(_class.getName().equals("long")||_class.getName().contains("Long")){
			return HReportFieldType.LONG;
		}else if(_class.getName().equals("float")||_class.getName().contains("Float")){
			return HReportFieldType.FLOAT;
		}else if(_class.getName().contains("BigDecimal")){
			return HReportFieldType.BIGDECIMAL;
		}else if(_class.getName().contains("Date")
				){
			return HReportFieldType.DATE;
		}else if(_class.getName().contains("JSON")
				){
			return HReportFieldType.JSON;
		}
		return HReportFieldType.UNSUPPORT;
	}

	public boolean isUnsupport(){return value==UNSUPPORT.getValue();}
	public boolean isNumber(){return value==INTEGER.getValue()||value==LONG.getValue();}
	public boolean isMoney(){return value==FLOAT.getValue()||value==BIGDECIMAL.getValue();}
	public boolean isDate(){return value== DATE.getValue();}
	public boolean isString(){return value==STRING.getValue();}
	public boolean isJSON(){return value==JSON.getValue();}
}
