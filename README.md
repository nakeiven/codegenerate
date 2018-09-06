# codegenerate
### freemaker代码自动生成工具

支持根据sql脚本生成实体类(entity)、mapper文件、dao接口、service接口和实现类、以及jsp页面等12中代码

#### 运行环境:

jdk 1.8

#### ide工具：

IntelliJ IDEA

#### 使用方法：

* `sqlsource.sql` sql脚本文件

例如：

```sql
--要求一个字段一行才能识别,另外comment中不能有英文逗号。
CREATE TABLE `biz_rel_shop_subdist` (
  `id` BIGINT(20) NOT NULL COMMENT '关系编号',
  `subdis_id` BIGINT(20) NOT NULL COMMENT '小区id',
  `shop_id` BIGINT(20) NOT NULL COMMENT '网点Id',
  CREATED_DATE TIMESTAMP NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
```



* `gentemplate.xml`

```XML
<obj pck="com.cainiao.wmsisc.common.dal" className="IscInventoryTransferOrderDetail"><!-- 正确填写包名和类名 -->
<!--
 pck对应的是包名，根据需要修改
 className 类名，根据需要修改
 -->
```

```XML
<!-- 生成代码类型:1:pojo code;2:dao; 3:action test; 4:mapper; 5:service; 6:service.impl; 
		7:spring xml; 8:struts view action; 9:struts save acton; 10:struts xml; 
		11、spring mvc的controller; 12:unit test -->
<outputType>110110000001</outputType>

<!-- 
outputType 需要生成哪些文件，总共12位，代码类型如上所述，需要生成的代码对应位置1
如上：110110000001 需要生成 pojo dao mapper service test
-->
```

```XML
<outPath>G:\Temp\src</outPath>
<!--生成文件存储地址，可自定义-->
```



