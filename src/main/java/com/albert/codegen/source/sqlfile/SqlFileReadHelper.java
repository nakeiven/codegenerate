/*
 * 文件名称: FileReadHelper.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: huangwb
 * 修改日期: 2012-3-1
 * 修改内容: 
 */
package com.albert.codegen.source.sqlfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.albert.codegen.util.Constants;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-1
 * @since DE6.0
 */
public class SqlFileReadHelper {
    
    static Logger logger = Logger.getLogger(SqlFileReadHelper.class);
    
    public static BufferedReader readSql(InputStream sqlIn) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(sqlIn, Constants.UTF_8));
        }
        catch (UnsupportedEncodingException e) {
            logger.error("脚本文件编码错误:", e);
            throw new RuntimeException("脚本文件编码错误:", e);
        }
        catch (Exception e) {
            logger.error("找不到脚本文件路径:", e);
            throw new RuntimeException("找不到脚本文件路径:", e);
        }
        return br;
    }
    
    /**
     * 读取一条sql语句
     * @param reader 升级文件读取
     * @return
     * @throws IOException
     * @author huangwb created on 2012-1-4 
     * @since CDS BMP 1.0
     */
    public static String readSql(BufferedReader reader) throws IOException  {
        StringBuffer sqlBuf = new StringBuffer();
        String sql = null;
        String line = reader.readLine();
        int index = -1;
        while(line != null){
            if(StringUtils.isNotBlank(line) && !(line.startsWith("--") || line.startsWith("/*"))) {
                index = line.indexOf("--");
                if (index > 0) {
                    line = line.substring(0, index);
                }
                sqlBuf.append(line.trim()).append(" ");
                if (line.trim().endsWith(";")) {//一个完整的sql;
                    sql = sqlBuf.toString().trim();
                    sql = replaceBlankspace(sql.substring(0, sql.length()-1));//去掉最后的分号，不然升级不了
                    if (StringUtils.isNotBlank(sql)) {
                        break;
                    } else {
                        sqlBuf.delete(0,sqlBuf.length());
                    }
                }
                
            }
            line = reader.readLine();
        }
        if (sqlBuf.length() > 0) {
            sql = sqlBuf.toString().trim();
            sql = replaceBlankspace(sql.substring(0, sql.length()-1));//去掉最后的分号，不然升级不了
        } 
        return replaceBlankspace(sql);
    }
    
    /**
     * 替换空白字符为空格
     * @param source 要被替换操作的字符串
     * @return
     * @author huangwb created on 2012-1-4 
     * @since CDS BMP 1.0
     */
    private static String replaceBlankspace(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source.replaceAll("\\s", " ").replaceAll(" +", " ");
        } else {
            return source;
        }
    }
}
