/*
 * 文件名称: ObjSqlFactory.java
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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.albert.codegen.bean.ArgInfo;
import com.albert.codegen.bean.Column;
import com.albert.codegen.bean.Obj;
import com.albert.codegen.source.DefaultObjFactory;
import com.albert.codegen.source.ObjFactory;
import com.albert.codegen.util.CodeGenUtil;


/**
 * 
 * @author <a href="mailto:huangwb@zjcds.com">huangwb</a> created on 2012-3-1
 * @since DE6.0
 */
public class ObjSqlFactory implements ObjFactory {
    
    Logger logger = Logger.getLogger(getClass());
    
    private static String PRIMARYKEY = "PRIMARY KEY";
    private static String FOREIGNKEY = "FOREIGN KEY";
    
   
    public Obj getObj(ArgInfo argInfo) {
        String filePath = argInfo.getSqlFilePath();
        InputStream sqlIn = CodeGenUtil.readStream(filePath);
        BufferedReader br = SqlFileReadHelper.readSql(sqlIn);
        String sql = "";
        try {
            sql = SqlFileReadHelper.readSql(br);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("末获取到表名称",e);
        }
        finally {
            try {
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        String tableName = captureTableName(sql);
        if (StringUtils.isNotBlank(tableName)) {
            argInfo.setTableName(tableName);
        }
        List<Column> columns = parseSql(sql);
        
        return new DefaultObjFactory().genObj(argInfo, columns);
    }

    private List<Column> parseSql(String sql) {
        List<Column> props = new ArrayList<Column>();
        String fbracket = "(";
        String lbracket = ")";
        int fBracketPos = 0;
        int lBracketPos = 0;
        fBracketPos = sql.indexOf(fbracket);
        lBracketPos = sql.lastIndexOf(lbracket);
        String subStr = sql.substring(fBracketPos+1,lBracketPos);
        String[] lines = subStr.split(",");
        String[] temps = null;
        Boolean isPrimary = false;
        String primaryKey = capturePrimarykey(subStr);
        for (String line : lines) {
            isPrimary = false;
            line = line.trim();
            if(line.toUpperCase().contains(PRIMARYKEY) && line.endsWith(",")) {
                isPrimary = true;
            } else if(line.toUpperCase().contains(PRIMARYKEY) || line.toUpperCase().contains(FOREIGNKEY)) {
                continue;
            }
            temps = line.split(" ");
            if(temps.length > 1) {
                String colName = CodeGenUtil.trimMySql(temps[0]);                
                if (StringUtils.containsIgnoreCase(primaryKey, colName)) {
                    isPrimary = true;
                }
                String colType = temps[1].replaceAll("\\(.*?\\)", "");
                int length = parserLength(temps[1]);
                
                Column column = new Column(colName, colType, isPrimary, length);
                column.setComment(parserComment(temps));
                props.add(column);
            } 
        }
        return props;
    }
    
    /**
     * 解析字段注释，如： COMMENT '分成id'
     * @return
     * @author zhangyz created on 2014-4-25
     */
    private String parserComment(String[] temps) {
        for (int ii = 0; ii < temps.length; ii++) {
            if ("COMMENT".equalsIgnoreCase(temps[ii])) {
                String comment = "";
                for (ii = ii + 1; ii < temps.length; ii++) {
                    comment += temps[ii] + " ";
                }
                comment = comment.trim();
                comment = CodeGenUtil.trimMySql(comment, "'");
                return comment;                
            }
        }        
        /*String hint = temps[temps.length - 2];//取倒数第二个
        if ("COMMENT".equalsIgnoreCase(hint)) {
            String comment = temps[temps.length - 1];
            comment = CodeGenUtil.trimMySql(comment, "'");
            return comment;
        }*/
        return null;
    }
    
    /**
     * 解析表达式中的字段长度
     * @param token 如：numeric(8)
     * @return 若没有则返回0，代表没有长度
     * @author zhangyz created on 2014-4-25
     */
    private int parserLength(String token) {
        int length = 0;
        int begin = token.indexOf("(");
        int end = token.indexOf(")", begin + 1);
        if (end > begin && end > 1) {
            String strlen = token.substring(begin + 1, end);
            if (strlen != null && strlen.length() > 0) {
                try {
                    length = Integer.parseInt(strlen);
                }
                catch(Exception ex) {
                    System.out.println("解析" + token + "中的字段长度失败:" + ex.getMessage());                            
                }
            }
        }
        return length;
    }
    
    /**
     * 获取主键
     * 
     * @param line
     * @return
     */
    private String capturePrimarykey(String line) {
        line = line.trim();
        if(line.toUpperCase().contains(PRIMARYKEY)) {
        	int start = line.toUpperCase().indexOf(PRIMARYKEY) + PRIMARYKEY.length();
        	int end = line.indexOf(",", start);
        	//start and end to capture the primary key line
        	if (end > 0) {
        		return line.substring(start,end);
        	}
        	return line.substring(start);
        }
        return "";
    }
    
    private String captureTableName(String sql) {
        String bracket = "(";
        int fBracketPos = 0;
        fBracketPos = sql.indexOf(bracket);
        String preStr = sql.substring(0, fBracketPos);
        preStr = preStr.trim();
        
        if (!StringUtils.containsIgnoreCase(preStr, "TABLE")) {
            throw new RuntimeException("末获取到表名称");
        }
        String[] words = preStr.split(" ");
        if (words.length > 0) {
            String ret = words[words.length-1];
            ret = CodeGenUtil.trimMySql(ret);
            return ret;
        }
        throw new RuntimeException("末获取到表名称");
    }
    
}
