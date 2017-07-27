/*
 * 文件名称: FileWriteUtil.java
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import com.albert.codegen.freemarker.FreeMarkerUtils;

/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-1
 * @since DE6.0
 */
public class FileWriteUtil {
    /**
     * 通过模板来生成java文件
     * @param data
     * @param templateName
     * @throws Exception
     * @author huangwb created on 2012-2-16 
     * @since DE6.0
     */
    public static void writeFile(Object data, String templateName,String filepath) throws Exception{
        String s = getContent(data, templateName);
        write(filepath,s);
    }
    
    public enum TemplateType {
        STRING,SQLFILE,METADATA,DATABASE; 
    }

    /**
     * @param data
     * @param templateName
     * @return
     * @throws Exception
     * @author huangwb created on 2012-2-16 
     * @since DE6.0
     */
    public static String getContent(Object data, String templateName) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("obj", data);
        String s = new FreeMarkerUtils().getTemplateContent(map,templateName);
        return s;
    }
    
    public static void write(String filepath,String content) throws IOException {
        OutputStreamWriter ow = null;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            //FileOutputStream fs = new FileOutputStream(file);
            ow = new OutputStreamWriter(new FileOutputStream(file), Constants.UTF_8);
            ow.write(content);
            ow.flush();
        }
        finally {
            if(null != ow) {
                ow.close();
            }
        }
    }
}
