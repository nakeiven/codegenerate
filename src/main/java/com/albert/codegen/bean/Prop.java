/*
 * 文件名称: Prop.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-2-16
 * 修改内容: 
 */
package com.albert.codegen.bean;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-2-16
 * @since DE6.0
 */
public class Prop {
    private String type;//javaType
    private String name;
    private String caption;
    
    private String columnName;
    private String jdbcType;
    
    public Prop(String type,String name) {
        this.type = type;
        this.name = name;
    }
    
    public Prop(String type, String name, String caption) {
        this.type = type;
        this.name = name;
        this.caption = caption;
    }
    
    public Prop(String type,String name, String jdbcType, String columnName) {
        this.type = type;
        this.name = name;
        this.jdbcType = jdbcType;
        this.columnName = columnName;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
    
    public String getCaption() {
        if (caption != null && caption.length() > 0)
            return caption;
        else
            return name;
    }    
    
    public String getComment() {
        if (caption == null)
            return "";
        return caption;
    }
    
    public void setComment(String comment) {
        this.caption = comment;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }    
}
