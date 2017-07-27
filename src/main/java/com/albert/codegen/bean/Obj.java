/*
 * 文件名称: Obj.java
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-2-16
 * @since DE6.0
 */
public class Obj {
    private String pak;
    private String className;
    private String classNameUpper;
    //private String classNameLower;
    private String tableName;
    private String primaryKey;//主键属性
    private String jdbcPK;//数据库中主键名
    private String pkJavaType;//主键的java类型
    private List<Prop> bizKeys;//业务联合主键
    private List<Prop> props;
    
    private ArgInfo argInfo;
    
    
    //----------------------------//
    private Date date = new Date();
    private String propTypeStr;
    private String propNameStr;
    
    public void initProps() {
        String[] strArr = propNameStr.split(",");
        String[] strTypeArr = propTypeStr.split(",");
        if(strArr.length != strTypeArr.length) {
            throw new RuntimeException("Number of Prop type and name not equal.");
        }
        props = new ArrayList<Prop>();
        for (int i=0;i<strArr.length;i++) {
            props.add(new Prop(strTypeArr[i],strArr[i]));
        }
    }
    
    public String getClassName() {
        return className;
    }
    
    public String getClassNameUpper() {
        return classNameUpper;
    }
    
    public void setClassName(String className) {
        this.className = className;
        this.classNameUpper = className.toUpperCase();
        //this.classNameLower = className.toLowerCase();
    }    

    public List<Prop> getProps() {
        return props;
    }
    
    public void setProps(List<Prop> props) {
        this.props = props;
    }

    public void setPropNameStr(String propNameStr) {
        this.propNameStr = propNameStr;
    }

    public String getPropNameStr() {
        return propNameStr;
    }

    public void setPropTypeStr(String propTypeStr) {
        this.propTypeStr = propTypeStr;
    }

    public String getPropTypeStr() {
        return propTypeStr;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;//.toUpperCase();
    }

    public String getTableName() {
        return tableName;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setPak(String pak) {
        this.pak = pak;
    }

    public String getPak() {
        return pak;
    }

    public void setJdbcPK(String jdbcPK) {
        this.jdbcPK = jdbcPK;
    }

    public String getJdbcPK() {
        return jdbcPK;
    }

    public ArgInfo getArgInfo() {
        return argInfo;
    }
    
    public void setArgInfo(ArgInfo argInfo) {
        this.argInfo = argInfo;
    }
    
    public String getPkJavaType() {
        return pkJavaType;
    }
    
    public void setPkJavaType(String pkJavaType) {
        this.pkJavaType = pkJavaType;
    }

    public List<Prop> getBizKeys()
    {
        return bizKeys;
    }

    public void setBizKeys(List<Prop> bizKeys)
    {
        this.bizKeys = bizKeys;
    }

    /**
     * 判断是否有附件文件索引字段
     * @return 0：没有；1：有
     * @author zhangyz created on 2013-9-4
     */
    public int getHaveFileField() {
        return (argInfo.getAttachFileFieldName() == null || argInfo.getAttachFileFieldName().length() == 0) ? 0 : 1;
    }
    
    /**
     * 是否有混合文件索引字段
     * @return
     * @author zhangyz created on 2013-9-4
     */
    public int getHaveMixField() {
        return (argInfo.getMixFileFieldName() == null || argInfo.getMixFileFieldName().length() == 0) ? 0 : 1;        
    }
    
    /**
     * 是否有 业务联合主键
     * @return
     * @Author:zhanggd created on 2014-6-18
     */
    public int getHaveBizkeys(){
        return (argInfo.getBizKeys() == null || argInfo.getBizKeys().length() == 0) ? 0 : 1;
    }
}
