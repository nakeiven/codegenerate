/*
 * 文件名称: DefaultObjFactory.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-3-5
 * 修改内容: 
 */
package com.albert.codegen.source;

import java.util.ArrayList;
import java.util.List;

import com.albert.codegen.bean.ArgInfo;
import com.albert.codegen.bean.Column;
import com.albert.codegen.bean.Obj;
import com.albert.codegen.bean.Prop;
import com.albert.codegen.util.CodeGenUtil;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-5
 * @since DE6.0
 */
public class DefaultObjFactory {

    /**
     * @param argInfo
     * @param strTableName
     * @param columns
     * @return
     * @author huangwb created on 2012-3-5 
     * @since DE6.0
     */
    public Obj genObj(ArgInfo argInfo, List<Column> columns) {
        String name = "";
        String jdbcName = "";
        String jdbcType = "";
        String cName = argInfo.getClassName();
        String strTableName = argInfo.getTableName();
        String pck = argInfo.getPck();
        Obj obj = new Obj();
        obj.setClassName(cName);
        //obj.setTableName(strTableName);
        obj.setPak(pck);
        obj.setTableName(strTableName);
        obj.setArgInfo(argInfo);
        
        List<Prop> props = new ArrayList<Prop>();
        for(Column column : columns) {
            jdbcName = column.getColumnName();
            name = CodeGenUtil.convertPropName(jdbcName);
            jdbcType = column.getJdbcType();
            jdbcType = convertJdbcType(jdbcType);
            Prop prop = new Prop(
                    CodeGenUtil.convertJdbcToJava(jdbcType),//得到java类型
                    name,
                    jdbcType,//
                    jdbcName
            );
            prop.setCaption(column.getComment());//字段描述
            props.add(prop);
           if(column.getIsPrimary()) {
               String pkName = prop.getName();
               if (pkName.equals("id") == false) {
                   pkName = "id";//主键定死用id表示了。
                   prop.setName(pkName);
               }         
               obj.setPrimaryKey(pkName);
               obj.setJdbcPK(jdbcName);
               
               String type = prop.getType();
               if (type.equalsIgnoreCase(Integer.class.getSimpleName())) {
                   if (column.getLength() > 9) {
                       type = Long.class.getSimpleName();
                       prop.setType(type);
                   }
               }
               obj.setPkJavaType(type);
           }
           else {
               String type = prop.getType();
               if (type.equalsIgnoreCase(Integer.class.getSimpleName())) {
                   if (column.getLength() > 9) {
                       type = Long.class.getSimpleName();
                       prop.setType(type);
                   }
               }
           }
        }
        obj.setProps(props);
        if(argInfo.getBizKeys() !=null || argInfo.getBizKeys().length()>0)
            setBizkeys(obj, argInfo.getBizKeys().trim());
        return obj;
    }
    
    /**
     * 设置业务联合主键字段
     * @param obj
     * @param bizKeys
     * @Author:zhanggd created on 2014-6-18
     */
    private void setBizkeys(Obj obj, String bizKeys)
    {
        List<Prop> bizKeysList = new ArrayList<>();
        List<Prop> props = obj.getProps();
        if (bizKeys.contains(",")){
            String[] array = bizKeys.trim().split(",");
            for (int i = 0; i < array.length; i++){
                iteratorProps(bizKeysList, props, array[i]);
            }
        }else{
            iteratorProps(bizKeysList, props, bizKeys);
        }
        obj.setBizKeys(bizKeysList);
    }

   
    /**
     * 设置业务联合主键辅助方法
     * @param bizKeysList
     * @param props
     * @param bizkeys
     * @Author:zhanggd created on 2014-6-19
     */
    private void iteratorProps(List<Prop> bizKeysList, List<Prop> props, String bizkeys)
    {
        for (Prop prop : props){
            if(bizkeys.equals(prop.getColumnName()))
                bizKeysList.add(prop); 
        }
    }

    //改为通用型,便于比较处理
    private String convertJdbcType(String jdbcType) {
        jdbcType = jdbcType.toUpperCase();
        if (jdbcType.equals("INT") || jdbcType.equals("BIGINT") || jdbcType.equals("TINYINT"))
            jdbcType = "INTEGER";
        else {
            jdbcType = jdbcType.replace("VARCHAR2", "VARCHAR");
            jdbcType = jdbcType.replace("NUMBER", "NUMERIC");//
            jdbcType = jdbcType.replace("DATETIME", "TIMESTAMP");
        }
        return jdbcType;
    }    
}
