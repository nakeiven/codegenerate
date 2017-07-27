/*
 * 文件名称: FactoryFactory.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-3-1
 * 修改内容: 
 */
package com.albert.codegen.source;

//import com.manfen.codegen.source.metadata.ObjMetaFactory;
import com.albert.codegen.source.sqlfile.ObjSqlFactory;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-1
 * @since DE6.0
 */
public class FactoryFactory {
    
    public static ObjFactory createFacotry(Integer facType){
        switch(facType) {
            case 1:
                return new ObjSqlFactory();
            case 2:
//                return new ObjMetaFactory();
            case 3:
                return null;
            case 4:
                return null;
            default:
                    return null;
        }
        
    } 
    
    public interface AryType {
        Integer STRING = 1;
        Integer SQLFILE = 2;
        Integer METADATA = 3;
        Integer DATABASE = 4; 
    }
    
    
}
