/*
 * 文件名称: ArgInfo.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-3-1
 * 修改内容: 
 */
package com.albert.codegen.bean;

import java.util.HashMap;
import java.util.Map;
import com.albert.codegen.util.TemlateEnum;

/**
 * 生成文件的请求参数
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-1
 * @since DE6.0
 */
public class ArgInfo {
    /**生成的类名*/
    private String className;
    
    /**表名*/
    private String tableName;
    
    /**中文名*/
    private String description;
    
    private String attachFileFieldName = null;
    
    private String mixFileFieldName = null;
    
    /**包名*/
    private String pck;
    
    private String genType;

    /**sql文件路径或者字段文件路径*/
    private String sqlFilePath;
    
    /**业务联合主键 */ 
    private String bizKeys;
    
    /**输出文件集合*/
    private Map<String,String> outputInfos = new HashMap<String,String>();
    
    public ArgInfo() {}
        
    public void setOutputInfo(OutputFile outputFile) {
        String outputType = outputFile.getOutputType();
        if (null != outputType) {
            for (int i = 0; i < outputType.length(); i++) {
                if ('0' == outputType.charAt(i)) {
                }
                else if ('1' == outputType.charAt(i)) {
                    addOutputCodeFile(i, outputFile);
                }
            }
        }
    }

    private void addOutputCodeFile(int tmpNo, OutputFile outputFile) {
        String className = outputFile.getClassName();
        String pck = outputFile.getPck();
        String filePath;
        String pckPath = pck.replaceAll("\\.", "/");
        // pck += "/";
        String filepath = null;
        for (TemlateEnum tmpEnum : TemlateEnum.values()) {
            if (tmpNo == tmpEnum.getTmpNo()) {
                if (tmpEnum.getIsJavaFile()) {
                    if (tmpNo == 2 || tmpNo == 11) {//测试类的
                        filePath = outputFile.getSrcTestPath();
                        filepath = filePath + pckPath + "/" + className;
                    }
                    else {
                        filePath = outputFile.getSrcMainPath();
                        filepath = filePath + pckPath + "/" + tmpEnum.getPckSux().replace(".", "/") + "/" + className;
                    }
                    filepath = filepath + tmpEnum.getSuffix();
                }
                else {
                    filePath = outputFile.getSrcConfPath();
                    String pre = tmpEnum.getPckSux() + "/";
                    String lastPack = outputFile.getLastPack();
                    if (lastPack != null && lastPack.length() > 0)
                        pre += lastPack + "/";
                    
                    if (tmpNo == 9) {
                        String sx = outputFile.getStrutsXmlFile();
                        if (sx != null && sx.length() > 0) {
                            filepath = filePath + pre + sx;
                        }
                    }
                    if (filepath == null) {
                        filepath = filePath + pre + className;//className前面可以加上包名： + pck + "." 
                        filepath = filepath + tmpEnum.getSuffix();
                    }
                }

                outputInfos.put(tmpEnum.getTmpName(), filepath);
                break;
            }
        }
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getDescription() {
        if (description != null && description.length() > 0)
            return description;
        else
            return tableName;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPck() {
        return pck;
    }
    
    public void setPck(String pck) {
        this.pck = pck;
    }
    
    public Map<String, String> getOutputInfos() {
        return outputInfos;
    }
    

    public void setGenType(String genType) {
        this.genType = genType;
    }

    public String getGenType() {
        return genType;
    }

    public void setSqlFilePath(String sqlFilePath) {
        this.sqlFilePath = sqlFilePath;
    }

    public String getSqlFilePath() {
        return sqlFilePath;
    }
    
    public String getAttachFileFieldName() {
        return attachFileFieldName;
    }
    
    public void setAttachFileFieldName(String attachFileFieldName) {
        this.attachFileFieldName = attachFileFieldName;
    }
    
    public String getMixFileFieldName() {
        return mixFileFieldName;
    }
    
    public void setMixFileFieldName(String mixFileFieldName) {
        this.mixFileFieldName = mixFileFieldName;
    }

    public String getBizKeys()
    {
        return bizKeys;
    }

    public void setBizKeys(String bizKeys)
    {
        this.bizKeys = bizKeys;
    }   
    
}
