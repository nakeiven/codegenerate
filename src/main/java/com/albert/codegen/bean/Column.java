/*
 * 文件名称: Column.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-3-5
 * 修改内容: 
 */
package com.albert.codegen.bean;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-5
 * @since DE6.0
 */
public class Column {
    private String columnName;
    private String jdbcType;
    private Boolean isPrimary;
    
    private String javaName;
    private String javaType;
    private int length = 0;
    private String comment;//字段中文名
    
    public Column(String columnName,String jdbcType) {
        this.columnName = columnName;
        this.jdbcType = jdbcType;
    }
    
    public Column(String columnName,String jdbcType,Boolean isPrimary, int length) {
        this.columnName = columnName;
        this.jdbcType = jdbcType;
        this.isPrimary = isPrimary;
        this.length = length;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
    public String getJdbcType() {
        return jdbcType;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public String getJavaName() {
        return javaName;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaType() {
        return javaType;
    }
    
    public int getLength() {
        return length;
    }   
    
    public void setLength(int length) {
        this.length = length;
    }   
    
}
