/*
 * 文件名称: ObjWriteHelper.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-3-5
 * 修改内容: 
 */
package com.albert.codegen.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.albert.codegen.bean.ArgInfo;
import com.albert.codegen.bean.Obj;
import com.albert.codegen.source.FactoryFactory;
import com.albert.codegen.source.ObjFactory;
import com.albert.modules.utils.xml.BDom4j;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-5
 * @since DE6.0
 */
public class ObjHelper {

    /**
     * 根据参数，把生成的对象写到文件
     * @param argInfo
     * @throws Exception
     * @author huangwb created on 2012-3-5 
     * @since DE6.0
     */
    public void write(ArgInfo argInfo) throws Exception {
        Obj obj = genObj(argInfo);
        Map<String,String> outputMap = argInfo.getOutputInfos();
        for (Map.Entry<String,String> entry : outputMap.entrySet()) {
            String tmplName = entry.getKey();
            if (tmplName.equals(TemlateEnum.TEMPSTUTSFILENAME.getTmpName()))
                appendStrutsFile(obj, tmplName, entry.getValue());
            else
                writeFile(obj, tmplName, entry.getValue());
        }
    }
    
    /**
     * 生成一个obj
     * @param argInfo
     * @return
     * @author huangwb created on 2012-3-5 
     * @since DE6.0
     */
    public Obj genObj(ArgInfo argInfo) {
        ObjFactory factory = FactoryFactory.createFacotry(Integer.parseInt(argInfo.getGenType()));
        Obj obj = factory.getObj(argInfo);
        return obj;
    }
    
    private void writeFile(Object data,String templateName,String filepath) throws Exception{
        FileWriteUtil.writeFile(data,templateName,filepath);
    }
    
    private void appendStrutsFile(Obj data,String templateName, String filepath) throws Exception {
        try {
            String s = FileWriteUtil.getContent(data, templateName);
            Document newDoc = DocumentHelper.parseText(s);
            Element packEle = (Element)newDoc.getRootElement().selectSingleNode("package");
            String packEleStr = packEle.asXML();
            packEle = DocumentHelper.parseText(packEleStr).getRootElement();
            
            File oldFile = new File(filepath);
            if (oldFile.exists()) {
                String oldFileString = FileUtils.readFileToString(oldFile, Constants.UTF_8);
                Document oldDoc = DocumentHelper.parseText(oldFileString);
                oldDoc.getRootElement().add(packEle); 
                //String lastXml = oldDoc.asXML();
                //FileWriteUtil.write(filepath, lastXml);
                BDom4j.documentToFile(oldDoc, filepath, Constants.UTF_8);
            }
            else{
                System.out.println("指定的struts配置文件不存在:" + filepath);
            }
        }
        catch(Exception ex) {
            System.out.println("合并到struts配置文件失败:" + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
