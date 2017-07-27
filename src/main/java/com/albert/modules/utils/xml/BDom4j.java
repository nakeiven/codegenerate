package com.albert.modules.utils.xml;

/**
 * Created on 2007-3-8 zjfanyi
 * 对dom4j操作的封装。
 * @author zhangyz。
 * update by zyz 20090403,对命名空间进行支持，否则selectsingleNode等返回始终为null。
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class BDom4j {

    public class MyResolver implements EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId) {
            InputSource source = new InputSource(new java.io.StringReader(""));//蒙骗解析器。
            return source;
        }
    }

    /** XML文件路径 */
    private String XMLPath = null;

    /** XML文档 */
    private Document document = null;

    private OutputFormat m_format = null;//xml输出时可以定义格式，主要是字符串。

    private Element m_root = null;

    private HashMap m_xmlMap = null;//xsd命名空间,可以有多个。

    private static String MAP_PRE = "s";

    /**
     * @return Returns the m_root.
     */
    public Element getM_root() {
        return m_root;
    }

    /**
     * @param m_root The m_root to set.
     */
    public void setM_root(Element m_root) {
        this.m_root = m_root;
    }

    public BDom4j() {
    }

    /**
     * 初始化xml文件
     * @param XMLPath 文件路径
     */
    public BDom4j(String XMLPath) {
        this.XMLPath = XMLPath;
    }

    public void openXML(Document doc) throws Exception {
        m_format = OutputFormat.createPrettyPrint();
        m_format.setEncoding("GBK");//可能不是doc原来的编码。
        this.document = doc;
        m_root = this.document.getRootElement();
        //System.out.println("openXML() successful ...");
        String uri = m_root.getNamespaceURI();
        if ((uri != null) && (uri.length() > 0)) {
            m_xmlMap = new HashMap();
            m_xmlMap.put(MAP_PRE, uri);//"http://www.springframework.org/schema/beans"
        }
    }

    /**
     * 打开文档
     */
    public void openXML() throws Exception {
        openXML(this.XMLPath, "GBK");
    }

    public void openXML(String path) throws Exception {
        openXML(path, "GBK");
    }

    /**
     * 打开文档，从中创建dom树
     * @param filePath 文档路径,不能是URI格式。
     */
    public void openXML(String filePath, String ecode) throws Exception {
        java.io.FileInputStream in = null;
        try {
            this.XMLPath = filePath;
            m_format = OutputFormat.createPrettyPrint();
            m_format.setEncoding(ecode);
            SAXReader reader = new SAXReader();
            reader.setEncoding(ecode);
            reader.setValidation(false);
            reader.setEntityResolver(new MyResolver());

            in = new java.io.FileInputStream(filePath);
            this.document = reader.read(in);
            m_root = this.document.getRootElement();
            //System.out.println("openXML() successful ...");
            String uri = m_root.getNamespaceURI();
            if ((uri != null) && (uri.length() > 0)) {
                m_xmlMap = new HashMap();
                m_xmlMap.put(MAP_PRE, uri);//"http://www.springframework.org/schema/beans"
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("openXML() Exception:" + e.getMessage());
        }
        finally {
            if (in != null)
                in.close();
        }
    }

    public void openXML(InputStream input, String encode) throws Exception {
        try {
            m_format = OutputFormat.createPrettyPrint();
            m_format.setEncoding(encode);
            SAXReader reader = new SAXReader();
            reader.setValidation(false);
            reader.setEntityResolver(new MyResolver());
            reader.setEncoding(m_format.getEncoding());
            this.document = reader.read(input);
            m_root = this.document.getRootElement();
            String uri = m_root.getNamespaceURI();
            if ((uri != null) && (uri.length() > 0)) {
                m_xmlMap = new HashMap();
                m_xmlMap.put(MAP_PRE, uri);//"http://www.springframework.org/schema/beans"
            }
            //System.out.println("openXML() successful ...");
        }
        catch (Exception e) {
            throw new Exception("openXML() Exception:" + e.getMessage());

        }
    }

    /**
     * 打开一个流，从中创建dom树;使用缺省的编码(GBK).
     * @param input
     * @throws Exception
     */
    public void openXML(InputStream input) throws Exception {
        openXML(input, "GBK");
    }

    /**
     * 创建一个xml文档，使用缺省的GBK编码集
     * @param rootName 根节点名称
     */
    public void createXML(String rootName) throws Exception {
        createXML(rootName, "GBK");
    }

    /**
     * 创建一个xml文档,使用指定的编码集
     * @param rootName
     * @param encode
     * @throws Exception
     * @return Element 根节点。
     */
    public Element createXML(String rootName, String encode) throws Exception {
        try {
            this.document = DocumentHelper.createDocument();
            m_format = OutputFormat.createPrettyPrint();
            m_format.setEncoding(encode);
            m_root = document.addElement(rootName);
            //System.out.println("createXML() successful...");
            return m_root;
        }
        catch (Exception e) {
            throw new Exception("createXML() Exception:" + e.getMessage());
        }
    }

    /**
     * 创建xml文档，并指定xml DOCTYPE.
     * @param rootName
     * @param docType
     */
    public void createXML(String rootName, String[] docType) throws Exception {
        try {
            this.document = DocumentHelper.createDocument();
            m_format = OutputFormat.createPrettyPrint();
            m_format.setEncoding("GBK");
            if ((docType != null) && (docType.length == 3))
                document.addDocType(docType[0], docType[1], docType[2]);
            m_root = document.addElement(rootName);
            //System.out.println("createXML() successful...");
        }
        catch (Exception e) {
            throw new Exception("createXML() Exception:" + e.getMessage());
            //System.out.println("createXML() Exception:" + e.getMessage());
        }
    }

    /**
     * 当xml具有命名空间时，安装通常的selectSingleNode返回都是null，此时需要在xpath中指定命名空间。
     * 本函数把原始的xpath串进行转换。
     * @param specName
     * @return
     */
    private String changeXpathWhenNameSpace(String specName) {
        StringBuffer xPath = new StringBuffer("");
        if (specName.startsWith("//")) {
            xPath.append("//");
            specName = specName.substring(2);
        }
        else if (specName.startsWith("/")) {
            xPath.append("/");
            specName = specName.substring(1);
        }
        String[] segs = org.apache.commons.lang.StringUtils.split(specName, "/");
        for (int ii = 0; ii < segs.length; ii++) {
            if (ii > 0)
                xPath.append("/");
            xPath.append(MAP_PRE);//如果子节点也有命名空间,则此处不支持。
            xPath.append(":");
            xPath.append(segs[ii]);
        }
        return xPath.toString();
    }

    /**
     * 获取指定名称的元素，返回一列表
     * @param specName xPath格式。
     * @return
     */
    public List getSpecNameElementList(String specName) {
        if (this.m_xmlMap != null) {
            XPath x = document.createXPath(changeXpathWhenNameSpace(specName));//如果子节点也有命名空间,则此处不支持。
            x.setNamespaceURIs(m_xmlMap);
            return x.selectNodes(m_root);
        }
        return m_root.selectNodes(specName);
    }

    /**
     * 获取指定名称的元素
     * @param specName xPath格式。
     * @return
     */
    public Node getSpecNameElement(String specName) {
        if (this.m_xmlMap != null) {
            XPath x = document.createXPath(changeXpathWhenNameSpace(specName));//"//s:constructor-arg/s:value"   
            x.setNamespaceURIs(m_xmlMap);
            Element e = (Element)x.selectSingleNode(m_root);
            return e;
        }
        return m_root.selectSingleNode(specName);
    }

    /**
     * 获取report 元素
     * @param ele element元素
     * @return
     */
    public Element getReport(Element ele) {
        return (Element)ele.selectSingleNode("OlapReport");
    }

    /**
     * 把一新元素插入到指定的元素中
     * @param parentEle  父元素
     * @param eleName  新插入的元素名
     * @param eleText 新插入的元素文本值
     * @param attributes  新插入的元素的属性集
     */
    public static Element addElementInSpecEle(Element parentEle, String eleName,
            String eleText, HashMap attributes) {
        Element ele = parentEle.addElement(eleName);
        if (isNotBlank(eleText))
            ele.setText(eleText);
        if (attributes != null) {
            Iterator it = attributes.keySet().iterator();
            while (it.hasNext()) {
                String key = (String)it.next();
                String value = (String)attributes.get(key);
                //if(StringUtils.isNotBlank(value))
                if (value != null)
                    ele.addAttribute(key, value);
            }
        }
        return ele;
    }

    public void addAttribute(String nodeName, String name, String value) {
        Element node = (Element)this.getSpecNameElement("//" + nodeName);
        node.addAttribute(name, value);
    }

    /**
     * 添加根节点的child
     * @param nodeName 节点名
     * @param nodeValue 节点值
     */
    public Element addNodeFromRoot(String nodeName, String nodeValue, HashMap attributes) {
        Element level1 = m_root.addElement(nodeName);
        if (isNotBlank(nodeValue))
            level1.addText(nodeValue);
        if (attributes != null) {
            Iterator it = attributes.keySet().iterator();
            while (it.hasNext()) {
                String key = (String)it.next();
                String value = (String)attributes.get(key);
                if (value != null)
                    level1.addAttribute(key, value);
            }
        }
        return level1;
    }

    /**
     * 保存文档
     */
    public void saveXML() {
        try {
            XMLWriter output = new XMLWriter(new FileOutputStream(XMLPath), m_format);
            output.write(document);
            output.close();
            //System.out.println("saveXML() successful ...");
        }
        catch (Exception e1) {
            System.out.println("saveXML() Exception:" + e1.getMessage());
        }
    }

    public InputStream saveXml2() throws Exception {
        try {
            XMLWriter output = new XMLWriter(new FileOutputStream(XMLPath), m_format);
            output.write(document);
            output.close();
            //System.out.println("saveXML() successful ...");
        }
        catch (Exception e1) {
        	e1.printStackTrace();
            //System.out.println("saveXML() Exception:" + e1.getMessage());
        }
        return new FileInputStream(new File(XMLPath));
    }

    /**
     * 把dom4j document对象保存至文件。
     * @param document
     * @param strFullName
     * @throws Exception
     */
    public static void documentToFile(Document document, String strFullName, String encode) throws Exception {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(encode);
        XMLWriter output = null;
        try {
            output = new XMLWriter(new FileOutputStream(strFullName), format);
            output.write(document);
        }
        catch (Exception ex) {
            throw new Exception("Dom文档保存至文件" + strFullName + "失败:" + ex.getMessage());
        }
        finally {
            if (output != null)
                output.close();
        }
    }

    /**
     * 把dom文档树导出到输入流中.
     * @param document
     * @param encode
     * @return
     * @throws Exception
     */
    public static InputStream documentToStream(Document document, String encode) throws Exception {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(encode);
        XMLWriter output = null;
        java.io.ByteArrayInputStream ret = null;
        try {
            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            output = new XMLWriter(out, format);
            output.write(document);
            ret = new java.io.ByteArrayInputStream(out.toByteArray());
            out.close();
            return ret;
        }
        catch (Exception ex) {
            throw new Exception("Dom文档保存至输出字节流失败:" + ex.getMessage());
        }
        finally {
            if (output != null)
                output.close();
        }
    }

    /**
     * 保存文档
     * @param toFilePath 保存路径
     */
    /*
    public void saveXML(String toFilePath) {
     try {
         XMLWriter output = new XMLWriter(new FileWriter(
                 new File(toFilePath)));
         output.write(document);
         output.close();
     } catch (Exception e1) {
         //System.out.println("saveXML() Exception:" + e1.getMessage());
     }
    }*/

    /**
     * 以InputStream的类型导本当前的Xml
     */
    public InputStream exportXml() throws Exception {
        return new FileInputStream(new File(XMLPath));
    }

    /**
     * 获得某个节点的值
     * @param nodeName 节点名称
     */
    public String getElementValue(String nodeName) {
        try {
            Node node = this.getSpecNameElement("//" + nodeName);
            if (node != null)
                return node.getText();
            return null;
        }
        catch (Exception e1) {
            System.out.println("getElementValue() Exception：" + e1.getMessage());
            return null;
        }
    }

    public List getElementList(String nodeName) {
        try {
            List list = this.getSpecNameElementList("//" + nodeName);
            return list;
        }
        catch (Exception e1) {
            System.out.println("getElementValue() Exception：" + e1.getMessage());
            return null;
        }
    }

    /**
     * 获得某个节点的子节点的值
     * @param nodeName
     * @param childNodeName
     * @return
     */
    public String getElementValue(String nodeName, String childNodeName) {
        try {
            Node node = null;
            if (this.m_xmlMap != null) {
                node = document.selectSingleNode("//" + MAP_PRE + ":" + nodeName + "/" + MAP_PRE + ":" + childNodeName);
            }
            else
                node = document.selectSingleNode("//" + nodeName + "/" + childNodeName);
            if (node != null)
                return node.getText();
            return null;
        }
        catch (Exception e1) {
            System.out.println("getElementValue() Exception：" + e1.getMessage());
            return null;
        }
    }

    /**
     * 设置一个节点的text
     * @param xpath
     * @param nodeValue 节点值
     */
    public void setXPathElementValue(String xpath, String nodeValue) {
        try {
            Node node = getSpecNameElement(xpath);
            node.setText(nodeValue);
        }
        catch (Exception e1) {
            System.out.println("setElementValue() Exception:" + e1.getMessage());
        }
    }

    /**
     * 设置一个节点的text
     * @param nodeName 节点名
     * @param nodeValue 节点值
     */
    public void setElementValue(String nodeName, String nodeValue) {
        try {
            Node node = getSpecNameElement("//" + nodeName);
            node.setText(nodeValue);
        }
        catch (Exception e1) {
            System.out.println("setElementValue() Exception:" + e1.getMessage());
        }
    }

    /**
     * 设置一个节点值
     * @param nodeName 父节点名
     * @param childNodeName 节点名
     * @param nodeValue 节点值
     */
    public void setElementValue(String nodeName, String childNodeName, String nodeValue) {
        try {
            Node node = getSpecNameElement("//" + nodeName + "/"
                    + childNodeName);
            node.setText(nodeValue);
        }
        catch (Exception e1) {
        	e1.printStackTrace();
            //System.out.println("setElementValue() Exception:" + e1.getMessage());
        }
    }

    /**
     * 
     * @param is
     * @return
     */
    public final static Document inputStream2Docment(InputStream is) throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        document = saxReader.read(is);
        return document;
    }

    /**
     * 判断是否为空
     * @param value
     * @return
     */
    private static boolean isNotBlank(String value) {
        if ((value != null) && (value.length() > 0))
            return true;
        return false;
    }

    public String getXMLPath() {
        return XMLPath;
    }

    public void setXMLPath(String path) {
        XMLPath = path;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
