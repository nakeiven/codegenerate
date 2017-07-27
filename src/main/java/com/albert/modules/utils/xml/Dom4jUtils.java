/*
 * 文件名称: Dom4jUtils.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: luojt
 * 修改日期: Jul 6, 2011
 * 修改内容: 
 */
package com.albert.modules.utils.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.albert.modules.entity.Constants;
import com.albert.modules.utils.CollectionUtils;
import com.albert.modules.utils.string.StringUtils;
import com.albert.comn.bean.TimeCursor;

/**
 * Dom4j工具类
 * 
 * @author luojt Jul 6, 2011
 */
public class Dom4jUtils {

    private static Logger logger = Logger.getLogger(Dom4jUtils.class);

    /**
     * 解析节点下的内容 &lt;KEY&gt;VALUE&lt;/KEY&gt; 到Map
     * 
     * @param node dom4j节点
     * @return 解析后的map
     * @author LuoJingtian created on 2011-11-3
     * @since DE 6.0
     */
    public static Map<String, String> parseNodeParam(Node node) {
        Map<String, String> params = new HashMap<String, String>();
        if (node != null) {
            Iterator<?> iter = ((org.dom4j.Branch) node).nodeIterator();
            Node nodeTemp;
            String paramName = null;
            String paramValue = null;
            while (iter.hasNext()) {
                nodeTemp = (Node) iter.next();
                paramName = nodeTemp.getName();
                paramValue = nodeTemp.getText();
                if (StringUtils.isNotEmpty(paramName)) {
                    params.put(paramName, paramValue);
                }
            }
        }
        return params;
    }

    /**
     * 解析节点信息&lt;Parameter Name="KEY" Value="VALUE" /&gt; 到Map
     * 
     * @param elems Element节点的List
     * @return Map
     * @author LuoJingtian created on 2011-11-3
     * @since DE 6.0
     */
    public static Map<String, String> parseAttributeParam(List<Element> elems) {
        Map<String, String> params = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(elems)) {
            String paramName = null;
            String paramValue = null;
            for (Element elem : elems) {
                paramName = elem.attributeValue("Name");
                paramValue = elem.attributeValue("Value");
                if (StringUtils.isNotEmpty(paramName)) {
                    params.put(paramName, paramValue);
                }
            }
        }
        return params;
    }
    
    /**
     * 解析节点信息&lt;Parameter Name="KEY" Value="VALUE" /&gt; 到Map
     * 
     * @param elems Element节点的List
     * @return Map
     * @author LuoJingtian created on 2011-11-3
     * @since DE 6.0
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseAttributeParam(Element parentParamElem) {
        if (parentParamElem != null) {
            List<Element> elems = parentParamElem.selectNodes("Param");
            return parseAttributeParam(elems);
        }
        return new HashMap<String, String>(0);
    }
    
    /**
     * 将KEY-VALUE对导出为 &lt;KEY&gt;VALUE&lt;/KEY&gt; XML节点
     * 
     * @param parentElem 参数导出的父节点
     * @param params 参数对
     * @author LuoJingtian created on 2011-11-3
     * @since DE 6.0
     */
    public static Element exportElementParam(Element parentElem, Map<String, String> params) {
        for (Map.Entry<String, String> param : params.entrySet()) {
            parentElem.addElement(param.getKey()).setText(param.getValue());
        }
        return parentElem;
    }
    
    /**
     * 将KEY-VALUE对导出为 &lt;Param Name="KEY" Value="VALUE"/&gt; XML节点
     * 
     * @param parentElem 参数导出的父节点
     * @param params 参数对
     * @author LuoJingtian created on 2011-11-3
     * @since DE 6.0
     */
    public static Element exportAttributeParam(Element parentElem, Map<String, String> params) {
        for (Map.Entry<String, String> param : params.entrySet()) {
            parentElem.addElement("Param").addAttribute(param.getKey(), param.getValue());
        }
        return parentElem;
    }
    

    /**
     * 指定节点下添加节点与值
     * @param ele
     * @param eleName
     * @param value
     * @throws Exception
     * @author LuoJingtian created on 2012-3-1 
     * @since SHK Framework 1.0
     */
    public static void addElementText(Element ele, String eleName, Object value) throws Exception {
        String strValue = null;
        if (value == null) {
            ele.addElement(eleName).setText("");
            return;
        }
        Class<?> valueType = value.getClass();
        if (valueType.equals(String.class))
            strValue = (String) value;
        else if (valueType.equals(Integer.class))
            strValue = ((Integer) value).toString();
        else if (valueType.equals(java.util.Date.class) || valueType.equals(java.sql.Timestamp.class)) {
            strValue = TimeCursor.InnerFormat.format((java.util.Date) value);
        }
        else
            throw new Exception("ElementAddor.addElementText不支持的数据类型:" + valueType.getName() + ";eleName:" + eleName);
        ele.addElement(eleName).setText(strValue);
    }

    /**
     * 获取节点的TextValue, 如果节点为空, 会返回null
     * 
     * @param node Node
     * @return 节点的TextValue
     * @author LuoJingtian created on 2011-11-29
     * @since DE 6.0
     */
    public static String getTextValue(Node node) {
        if (node != null) {
            return node.getText();
        }
        return null;
    }
    
    /**
     * 解析xml字符串为element
     * @param xml
     * @return
     * @throws DocumentException
     * @author huangwb created on 2012-1-4 
     * @since
     */
    public static Element parseText(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml).getRootElement();
    }

    /**
     * 获取指定节点的子节点文本值
     * @param node 节点
     * @param childNodeName 子节点名称
     * @return 子节点文本值
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static String getChildNodeValue(Node node, String childNodeName) {
        Element childNode = (Element) node.selectSingleNode(childNodeName);
        if (childNode != null) {
            return childNode.getTextTrim();
        }
        return null;
    }

    /**
     * 获取子节点
     * @param root
     * @param name
     * @return
     */
    public static Element getChild(Element root, String name) {
        Element ret = (Element)root.selectSingleNode(name);
        if (ret == null)
            ret = root.addElement(name);
        return ret;
    }
    
    /**
     * 获取指定节点的属性值
     * @param node 节点
     * @param attributeName 属性名称
     * @return
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static String getAttributeValue(Node node, String attributeName) {
        if (node != null && node instanceof Element) {
            return ((Element)node).attributeValue(attributeName);
        }
        return null;
    }
    
    /**
     * 获取指定元素的属性值
     * @param element 节点元素
     * @param attributeName 属性名称
     * @return 属性文本值
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static String getAttributeValue(Element element, String attributeName) {
        if (element != null) {
            return element.attributeValue(attributeName);
        }
        return null;
    }
    
    /**
     * 获取XML文档指定xPath下的文本值
     * @param doc XML文档对象
     * @param xPath 路径
     * @return 文本值
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static String getTextValue(Document doc, String xPath) {
        return getElementByXPath(doc, xPath).getTextTrim();
    }

    /**
     * 获取XML文档指定xPath节点对应的属性文本值
     * @param doc XML文档对象
     * @param xPath 路径
     * @param attrName xPath 路径的属性名称
     * @return xPath节点对应的属性文本值
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static String getAttributeValue(Document doc, String xPath, String attrName) {
        return getElementByXPath(doc, xPath).attributeValue(attrName);
    }

    /**
     * 设置XML文档指定xPath节点对应的属性文本值
     * @param doc XML文档对象
     * @param xPath 路径
     * @param attrName xPath 路径的属性名称
     * @param attrValue 要设置的属性值
     * @return xPath对应的节点元素
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static Element setAttributeValue(Document doc, String xPath, String attrName, String attrValue) {
    	Element elem = getElementByXPath(doc, xPath);
    	if (elem != null)
    		elem.addAttribute(attrName, attrValue);
        return elem;
    }
    
    /**
     * 
     * @param doc
     * @param xPath
     * @param value
     * @return
     * @author Huxiao created on 2012-2-29 
     * @since DE 6.0
     */
    public static Element setText(Document doc, String xPath, String value) {
    	Element elem = getElementByXPath(doc, xPath);
    	if (elem != null)
    		elem.setText(value);
    	return elem;
    }

    /**
     * 获取指定XML文档的xPath节点对应的节点元素
     * @param doc XML文档对象
     * @param xPath 路径
     * @return xPath对应的节点元素
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static Element getElementByXPath(Document doc, String xPath) {
        return (Element) doc.selectSingleNode(xPath);
    }
    
    /**
     * 获取指定节点的xPath节点对应的节点元素
     * @param node 节点对象
     * @param xPath 路径
     * @return xPath对应的节点元素
     * @author LuoJingtian created on 2012-2-29 
     * @since SHK Framework 1.0
     */
    public static Element getElementByXPath(Node node, String xPath) {
        return (Element) node.selectSingleNode(xPath);
    }

    /**
     * 解析xml字符串为Document
     * 
     * @param xml XmlString
     * @return Document
     * @throws DocumentException
     * @author huangwb created on 2012-1-4
     * @since
     */
    public static Document getDocumentFromXmlString(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }
    
    /**
     * 解析xml字符串为element
     * 
     * @param xml
     * @return
     * @throws DocumentException
     * @author huangwb created on 2012-1-4
     * @since
     */
    public static Element getElementFromXmlString(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml).getRootElement();
    }

    /**
     * 从XML文件获取Document
     * 
     * @param filePath 文件路径
     * @return Document
     * @throws DocumentException
     * @author LuoJingtian created on 2012-2-29
     * @since SHK Framework 1.0
     */
    public static Document getDocumentFromFile(String filePath) throws DocumentException {
        Document doc;
        DocumentFactory docFactory = new DocumentFactory();
        SAXReader reader = new SAXReader(docFactory);
        doc = reader.read(new File(filePath));
        return doc;
    }
    
    /**
     * 从XML文件获取Document
     * 
     * @param filePath 文件路径
     * @return Document
     * @throws DocumentException
     * @author LuoJingtian created on 2012-2-29
     * @since CDS Framework 1.0
     */
    public static Document getDocumentFromFile(InputStream in) throws DocumentException {
        Document doc;
        DocumentFactory docFactory = new DocumentFactory();
        SAXReader reader = new SAXReader(docFactory);
        doc = reader.read(in);
        try {
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 从XML文件获取Document
     * 
     * @param filePath 文件路径
     * @param namespaceURIs 命名空间
     * @return Document
     * @throws DocumentException
     * @author LuoJingtian created on 2012-2-29
     * @since SHK Framework 1.0
     */
    public static Document getDocumentFromFile(String filePath, Map<String, String> namespaceURIs)
            throws DocumentException {
        Document doc;

        DocumentFactory docFactory = new DocumentFactory();
        docFactory.setXPathNamespaceURIs(namespaceURIs);

        SAXReader reader = new SAXReader(docFactory);
        doc = reader.read(new File(filePath));
        return doc;
    }

    /**
     * 保存XML文档对象到文件
     * @param doc 文档对象
     * @param filePath 文件路径
     * @throws IOException
     * @author LuoJingtian created on 2012-3-1 
     * @since SHK Framework 1.0
     */
    public static void saveDocument(Document doc, String filePath) throws IOException {
        XMLWriter writer = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(Constants.GBK); // 指定XML编码
            writer = new XMLWriter(new FileWriter(filePath), format);
            writer.write(doc);
        }
        catch (Exception e) {
            System.err.println(e);
        }
        finally {
            Dom4jUtils.closeQuietly(writer);
        }
    }

    /**
     * 安静的关闭XMLWriter, 如果关闭IO流异常时使用默认日志记录器写入默认的错误信息到日志.
     * 
     * @param xmlwriter XMLWriter
     */
    public static void closeQuietly(XMLWriter xmlwriter) {
        closeQuietly(xmlwriter, logger, "关闭XMLWriter发生异常.");
    }

    /**
     * 安静的关闭XMLWriter, 如果关闭IO流异常时使用给定日志记录器写入默认的错误信息到日志.
     * 
     * @param xmlwriter XMLWriter
     * @param logger 日志记录器
     */
    public static void closeQuietly(XMLWriter xmlwriter, Logger logger) {
        closeQuietly(xmlwriter, logger, "关闭XMLWriter发生异常.");
    }

    /**
     * 安静的关闭XMLWriter, 如果关闭IO流异常时使用给定日志记录器写入默认的错误信息到日志.
     * 
     * @param xmlwriter XMLWriter
     * @param message 关闭XMLWriter异常时写入错误日志的信息
     */
    public static void closeQuietly(XMLWriter xmlwriter, String message) {
        closeQuietly(xmlwriter, logger, message);
    }

    /**
     * 安静的关闭XMLWriter, 如果关闭IO流异常时使用给定日志记录器写入给定的错误信息到日志.
     * 
     * @param xmlwriter XMLWriter
     * @param logger 日志记录器
     * @param message 关闭XMLWriter异常时写入错误日志的信息
     */
    public static void closeQuietly(XMLWriter xmlwriter, Logger logger, String message) {
        if (xmlwriter != null) {
            try {
                xmlwriter.close();
            }
            catch (IOException e) {
                logger.warn(message, e);
            }
        }
    }
}
