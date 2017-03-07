#### 一、HReport简介
##### 1. HReport适用于什么场景？
- 存储需求经常变动（例如数据抓取的存储）
- 存储多种数据，每种数据量不大（例如每天根据不同维度出的统计报表）

##### 2. HReport的特点
- 表结构就是类，类只要extend HReportTable 
例如：
```
@Data
@HReportTableAna(deleteOldDataIfChanged = true)
public class TestBean extends HReportTable {
        @HReportFieldAna(isKey = true)
        private Long id;
        @HReportFieldAna(isKey = false)
        private String name;
        private Integer sex;
        private BigDecimal account;
        private Float height;
        private Date time;
        private JSONObject json1;
        private JSONObject json2;
}
```

- 支持主键

```
 主键字段只要使用注解 HReportFieldAna(isKey = true)
```


- 支持数据类型

```
目前字段支持4种：
数字型（Long,Float,Integer,BigDecimal）
字符串类型String
时间类型Date
JSON类型JSONObject

```

```
        TestBean t=new TestBean();
        t.setDataVersion(String.valueOf(System.currentTimeMillis()));
        t.setId(2l);
        t.setName("hello");
        t.setSex(1);
        t.setHeight(1.84f);
        t.setAccount(new BigDecimal(100.01));
        t.setTime(new Date());
        JSONObject json=new JSONObject();
        json.put("key",1);
        json.put("value","value");
        t.setJson1(json);

        reportService.saveTableData(t,new HReporter("test","127.0.0.1"));
```

- 支持查询方式

```
普通查询方式：
   //可以按照字段定义中数据查询，支持多种比较方式
   HReportQuery query=new HReportQuery(TestBean.class);
   query.greaterThan("height",Float.valueOf(1.65f)).lessThanOrEqualTo("height",Float.valueOf(1.85f));
   List<TestBean> list= reportService.queryTableData(query,new HReporter("test","127.0.0.1"));
        
查询最后一次插入的数据：
   //比如一次性插入了10条数据，可以给这些数据赋予同一个version号，使用queryLastedData可以一次性把这10条数据查出来
   List<TestBean> list= reportService.queryLastedData(TestBean.class);
   System.out.println(list==null?"no found":list.toString());

```

#### 3.安装方法

```
git@github.com:hubian/hreport.git
cd hreport
mvn -U clean install

```


```
<dependency>
            <groupId>org.hubian</groupId>
            <artifactId>hreport-client</artifactId>
</dependency>
<dependency>
            <groupId>org.hubian</groupId>
            <artifactId>hreport-service</artifactId>
</dependency>
```


#### 4.原理介绍

- 统一的数据结构
  
```
report_table_schema 存储表的基本信息
  `table_id` INT NOT NULL AUTO_INCREMENT,
  `table_name` VARCHAR(128) NOT NULL COMMENT '表名和类名一致',
  `sign_code` VARCHAR(128) NOT NULL COMMENT '表结构签名',
  `create_date` DATE NOT NULL COMMENT '创建时间',
  `create_ip` VARCHAR(32) NOT NULL COMMENT '创建IP',
  `update_date` DATE NOT NULL COMMENT '更新时间',
  `update_ip` VARCHAR(32) NOT NULL COMMENT '更新IP',
  `lasted_version` VARCHAR(32) NOT NULL COMMENT '最新版本号',

report_field_schema 存储类属性和表字段的关联
`field_id` INT NOT NULL AUTO_INCREMENT ,
  `table_id` INT NOT NULL,
  `field_name` VARCHAR(32) NOT NULL COMMENT '属性名称',
  `field_type` INT NOT NULL COMMENT 'field类型：数字/字符串',
  `data_field` VARCHAR(6) NOT NULL COMMENT 'report_data中的字段',
  `primary_key` BOOL NOT NULL DEFAULT FALSE COMMENT '当前属性是否是主键',
  
report_data 存储具体的数据，可以根据需求调整字段组合
`data_id` BIGINT NOT NULL AUTO_INCREMENT,
  `table_id` INT  NOT NULL COMMENT '表ID',
  `data_version` varchar(32) NOT NULL,
  `lf1` BIGINT COMMENT '数字型字段1',
  `lf2` BIGINT COMMENT '数字型字段2',
  `lf3` BIGINT COMMENT '数字型字段3',
  `lf4` BIGINT COMMENT '数字型字段4',
  `lf5` BIGINT COMMENT '数字型字段5',
  `lf6` BIGINT COMMENT '数字型字段6',
  `lf7` BIGINT COMMENT '数字型字段7',
  `lf8` BIGINT COMMENT '数字型字段8',
  `lf9` BIGINT COMMENT '数字型字段9',
  `lf10` BIGINT COMMENT '数字型字段10',
  `sf1` VARCHAR(256) COMMENT '字符串字段1',
  `sf2` VARCHAR(256) COMMENT '字符串字段2',
  `sf3` VARCHAR(256) COMMENT '字符串字段3',
  `sf4` VARCHAR(256) COMMENT '字符串字段4',
  `sf5` VARCHAR(256) COMMENT '字符串字段5',
  `sf6` VARCHAR(256) COMMENT '字符串字段6',
  `sf7` VARCHAR(256) COMMENT '字符串字段7',
  `sf8` VARCHAR(256) COMMENT '字符串字段8',
  `sf9` VARCHAR(256) COMMENT '字符串字段9',
  `sf10` VARCHAR(256) COMMENT '字符串字段10',
  `jf1` VARCHAR(2048) COMMENT 'JSON字段1',
  `jf2` VARCHAR(2048) COMMENT 'JSON字段2',
```

- 插入逻辑

```
调用reportService.saveTableData方法时：
会查询是否存在这个表对应的schema信息，如果没有，则插入report_table_schema和report_field_schema
插入数据前会查询report_Data是否已经有对应主键的数据，如果有，则删除

```

- 查询逻辑

```
普通查询逻辑queryTableData方法时，会将HReportQuery信息映射为对应的mybatis方法
version查询时，会查询report_table_schema中的lasted_version

拿到report_data后，再根据report_field_schema的记录，将DB数据转成Class属性

```
