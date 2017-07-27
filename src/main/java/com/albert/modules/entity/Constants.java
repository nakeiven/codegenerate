/*
 * 文件名称: Constants.java
 * 版权信息: Copyright 2001-2012 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2012-1-18
 * 修改内容: 
 */
package com.albert.modules.entity;

import java.text.SimpleDateFormat;

/**
 * 常量定义
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on 2012-1-18
 * @since CDS BMP 1.0
 */
public class Constants {
    
    /** 编码 GBK */
    public static final String GBK = "GBK";
    public static final String defaultCode = "GBK";
    public static final String ISO8859_1 = "ISO8859_1";
    public static final String g_Code = defaultCode;
    
    /** UTF-8 */
    public static final String UTF8 = "UTF-8";
    
    /** 默认编码 GBK */
    public static final String DEFAULT_CHARSET = GBK;
    
    /** 默认响应编码 */
    public static final String DEFAULT_RESPONSE_CHARSET = UTF8;

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    
    public static final String INNER_DATAFORMAT = "yyyyMMdd HH:mm:ss";
    public static final SimpleDateFormat InnerFormat = new SimpleDateFormat(INNER_DATAFORMAT);// 内部使用的日期格式。
 
    public static final String DEFAULT_IMAGE_WIDTH = "app.image.width"; // 缩略图压缩后图片宽度
    public static final String DEFAULT_IMAGE_HEIGHT = "app.image.height"; // 缩略图压缩后图片高度
    public static final String DEFAULT_IMAGE_PROPORTION = "app.image.proportion"; // 缩略图压缩比例(0~1)
    
    public static final String FILE_HTML_SUFFIX = ".html";
}
