/*
 * 文件名称: FreeMarkerUtils.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Huangweibing
 * 修改日期: 2011-10-9
 * 修改内容: 
 */
package com.albert.codegen.freemarker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">Huangweibing</a> created on 2011-10-9
 * @since DE 5.2
 */
public class FreeMarkerUtils {
    private static Configuration cfg;
    Logger logger = Logger.getLogger(getClass());
    
    public static Configuration getConfiguration(){
        //创建一个Configuration实例
        if (cfg == null){
            synchronized(FreeMarkerUtils.class){
                if (cfg == null){
                    cfg = new Configuration();
                }
            }
        }
         //设置FreeMarker的模版文件位置
        cfg.setClassForTemplateLoading(FreeMarkerUtils.class, "");
        return cfg;
    }
    
    /**
     * 根据内容与模板名称生成xml格式文本
     * @param data 数据
     * @param templateName 模板名称
     * @return
     * @throws Exception
     * @author Huangweibing created on 2011-10-10 
     * @since DE 5.2
     */
    public String getTemplateContent(Map<String,Object> data,String templateName) throws Exception{
        Configuration cfg = FreeMarkerUtils.getConfiguration();
        try {
            Template template = cfg.getTemplate(templateName,"utf-8");
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            template.process(data, new PrintWriter(baos));
            return baos.toString();
        }
        catch (IOException e) {
            logger.warn("读取模块文件出错",e);
            throw new Exception("读取模块文件出错",e);
        }
        catch (TemplateException e) {
            logger.warn("模块格式化出错",e);
            throw new Exception("模块格式化出错",e);
        }
    }
}
