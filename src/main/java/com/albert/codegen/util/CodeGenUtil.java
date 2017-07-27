/*
 * 文件名称: MetaUtil.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-2-16
 * 修改内容: 
 */
package com.albert.codegen.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-2-16
 * @since DE6.0
 */
public class CodeGenUtil {
    public static String convertJdbcToJava(String jdbcType) {
        return TypeEnum.getJavaTypeByJdbcType(jdbcType);
    }
    
    public static String convertPropName(String propName) {
        if (StringUtils.isNotBlank(propName)) {
            //对于包含有下划线的字段，进行除下划线，并把下划线后一个字母大写
            //对于不满足的不作处理
            if (propName.contains("_")){
                propName = propName.toLowerCase();
            }
            propName = removeLineUpAlpa(propName);
        }
        return propName;
    }
    
    private static String removeLineUpAlpa(String str) {
        if (str.contains("_")){
            int i = str.indexOf("_");
            String ret = str.substring(0,i);
            String ret2 = str.substring(i+1,i+2);
            String ret3 = str.substring(i+2,str.length());
            return removeLineUpAlpa(ret+ret2.toUpperCase()+ret3);
        }
        return str;
    }
    
    public enum TypeEnum {
        VARCHAR("VARCHAR", "String"), 
        VARCHAR2("VARCHAR2", "String"), 
        DATE("DATE", "Date"), 
        DATETIME("TIMESTAMP", "Date"), 
        NUMBER("NUMBER", "Integer"), 
        INTEGER("INTEGER", "Integer"), 
        NUMRIC("NUMERIC", "Integer"), 
        DOUBLE("DOUBLE", "Double"), 
        DECIMAL("DECIMAL", "Double"), 
        OTHER("OTHER", "String");

        private String jdbcType;
        private String javaType;

        public static String getJavaTypeByJdbcType(String jdbcType) {
            String javaType = OTHER.getJavaType();
            TypeEnum[] types = TypeEnum.values();
            for (TypeEnum type : types) {
                if (type.getJdbcType().equals(jdbcType)) {
                    return type.getJavaType();
                }
            }
            return javaType;
        }

        private TypeEnum(String jdbcType, String javaType) {
            this.setJdbcType(jdbcType);
            this.setJavaType(javaType);
        }

        public void setJdbcType(String jdbcType) {
            this.jdbcType = jdbcType;
        }

        public String getJdbcType() {
            return jdbcType;
        }

        public void setJavaType(String javaType) {
            this.javaType = javaType;
        }

        public String getJavaType() {
            return javaType;
        }
    }
    
    /**
     * 把mysql表名、字段名前后的引号去掉
     * @param name
     * @return
     * @author zhangyz created on 2014-4-25
     */
    public static String trimMySql(String name) {
        String mc = "`";
        return trimMySql(name, mc);
    }
    
    public static String trimMySql(String name, String trimChar) {
        if (name.startsWith(trimChar))
            name = name.substring(1);
        if (name.endsWith(trimChar))
            name = name.substring(0, name.length() - 1);
        return name;
    }

    /**
     * 从文件路径或类路径中读取流
     * @param path
     * @return
     * @author zhangyz created on 2014-4-25
     */
    @SuppressWarnings("resource")
    public static InputStream readStream(String path) {
        InputStream sqlIn = null;
        if (path.startsWith("file://")){
            try {
                java.net.URI uri = new java.net.URI(path);
                sqlIn = new FileInputStream(new File(uri));
            }
            catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            sqlIn = CodeGenUtil.class.getResourceAsStream(path);
        }
        return sqlIn;
    }
}
