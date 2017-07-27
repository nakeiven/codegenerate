/*
 * 文件名称: TmpParseUtil.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-3-1
 * 修改内容: 
 */
package com.albert.codegen.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import com.albert.modules.utils.xml.Dom4jUtils;

import com.albert.codegen.bean.ArgInfo;
import com.albert.codegen.bean.OutputFile;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-1
 * @since DE6.0
 */
public class TmpParseUtil {
    /**
     * 从XML文件获取Document
     * 
     * @param filePath 文件路径
     * @return Document
     * @throws DocumentException
     * @author LuoJingtian created on 2012-2-29
     * @since CDS Framework 1.0
     */
    public static Document getDocumentFromFile(String filePath) throws DocumentException {
        InputStream in = CodeGenUtil.readStream(filePath);
        return Dom4jUtils.getDocumentFromFile(in);
    }

    public static List<ArgInfo> parseXml(String filePath) throws DocumentException {
        Document doc = getDocumentFromFile(filePath);
        
//        System.out.println(doc.asXML());
        @SuppressWarnings("unchecked")
        List<Node> nodes = doc.selectNodes("gen/obj");
//        System.out.println(nodes.size());
        ArgInfo argInfo = null;
        List<ArgInfo> argInfos = new ArrayList<ArgInfo>();
        for (Node node : nodes) {
            argInfo = initObj(node);
            argInfos.add(argInfo);
        }
        return argInfos;
    }
    
    private static ArgInfo initObj(Node node) {
        ArgInfo info = new ArgInfo();
        Element nodeObj = (Element)node;
        String className = nodeObj.attributeValue("className");
        String pck = nodeObj.attributeValue("pck");
        if (pck == null)
            throw new RuntimeException("包名不能为空!");
        if (pck.endsWith("."))
            pck = pck.substring(0, pck.length() - 1);
        
        String lastPck = pck;
        int lastDot = pck.lastIndexOf(".");
        if (lastDot > 0)
            lastPck = pck.substring(lastDot + 1);
        
        Map<String,String> attr = Dom4jUtils.parseNodeParam(node);
        
        String genType = attr.get("genType");
        String sqlFilePath = attr.get("sqlFilePath");
        String outputType = attr.get("outputType");
        String outPath = attr.get("outPath");
        String tableName = attr.get("tableName");
        String description = attr.get("description");
        String bizKeys = attr.get("bizKeys");

        OutputFile outputFile = new OutputFile();
        outputFile.setClassName(className);
        outputFile.setStrutsXmlFile(attr.get("strutsXmlFile"));
        outputFile.setOutputType(outputType);
        outputFile.setPck(pck);
        
        outputFile.setProjectPath(outPath, lastPck);
        //可以再覆盖
        String srcMainPath = attr.get("srcMainPath");
        if (srcMainPath != null)
            outputFile.setSrcMainPath(srcMainPath);
        
        String srcTestPath = attr.get("srcTestPath");
        if (srcTestPath != null)
            outputFile.setSrcTestPath(srcTestPath);
        
        String srcConfPath = attr.get("srcConfPath");
        if (srcConfPath != null)
            outputFile.setSrcConfPath(srcConfPath);        
        
        info.setOutputInfo(outputFile);
        info.setPck(pck);
        info.setClassName(className);
        info.setSqlFilePath(sqlFilePath);
        info.setGenType(genType);
        info.setTableName(tableName);
        info.setDescription(description);
        info.setBizKeys(bizKeys);
        
        info.setAttachFileFieldName(attr.get("attachFileFieldName"));
        info.setMixFileFieldName(attr.get("mixFileFieldName"));
        
        return info;
    }
    
}
