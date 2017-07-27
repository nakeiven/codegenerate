/*
 * 创建日期 2005-7-4
 *
 * 更改所生成文件模板为
 * 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */
package com.albert.modules.utils.string;

/**
 * @author Administrator
 * 
 * 更改所生成类型注释的模板为 窗口 > 首选项 > Java > 代码生成 > 代码和注释
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.dom4j.Node;

/**
 * 
 */
public class StringUtils {

    public static boolean isEmpty(String value) {
        return value == null || "".equals(value.trim());
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * 判断指定字符串是否包含中文字符
     * 
     * @param str 字符串
     * @return
     * @author LuoJingtian created on 2012-2-22
     * @since DE 6.0
     */
    public static boolean containsChinese(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }

        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解析得到encoding的具体编码
     * 
     * @param encoding
     * @return 编码
     */
   /* public static String parseEncoding(String encoding) {

        String regexp = "encoding=\"(gBK|utf-8|gb2312|ISO-8859-1)\"";

        PatternCompiler compiler = new Perl5Compiler();
        org.apache.oro.text.regex.Pattern pattern = null;
        try {
            pattern = compiler.compile(regexp, Perl5Compiler.CASE_INSENSITIVE_MASK);
        }
        catch (MalformedPatternException e) {
            e.printStackTrace();
        }

        PatternMatcher matcher = new Perl5Matcher();
        if (matcher.contains(encoding, pattern)) {
            MatchResult result = matcher.getMatch();
            return result.group(1);
            // System.out.println("encoding:"+result.group(1));
        }

        return null;
    }*/

    /**
     * unicode编码转成中文 例子："&#20013;"-->"中"
     * 
     * @return
     */
    public static String unicode2Cn(String src) {
        String regexp = "&#\\d{5};";
        StringBuilder out = new StringBuilder();
        int last = 0;
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(src);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String match = src.substring(start, end);

            String unicode = match.substring(2, 7);
            Character c = Character.valueOf((char) Integer.valueOf(unicode).intValue());

            String pre = "";
            if (start != 0)
                pre = src.substring(last, start);
            out.append(pre);
            out.append(c.toString());
            last = end;
        }
        if (last != src.length())
            out.append(src.substring(last));
        return out.toString();
    }

    /**
     * 将数字格式化为前空位补0的字符串，如果数字位数大于允许最大位数，头部数字将被截去。<br>
     * eg :<br>
     * 输入 1,5 --- 输出 "00001"<br>
     * 输入 123,2 --- 输出 "23"
     * 
     * @param num 被转化数字
     * @param maxbit 允许最大位数（返回字符串最大长度)
     */
    public static final String formatToZeroBefore(long num, int maxbit) {
        StringBuffer buf = new StringBuffer(maxbit);
        String strNum = "" + num;
        if (strNum.length() < maxbit) {
            for (int i = 0; i < (maxbit - strNum.length()); i++)
                buf.append('0');
            buf.append(strNum);
        }
        else
            buf.append(strNum.substring(strNum.length() - maxbit, strNum.length()));
        return new String(buf);
    }

    public static final String replace(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    @Deprecated
    public static final boolean isEmptyString(String s) {
        return org.apache.commons.lang.StringUtils.isBlank(s);
    }

    public static final String convertNumberToString(Number value) {
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingUsed(false);
        String result = formatter.format(value.doubleValue());
        return result;
    }

    /**
     * 将String压缩 example:com.jbbis.aic.alteration.entity.AlterRequisition 中
     * integrateOpinion,getIntegrateOpinionInBytes(),setIntegrateOpinionInBytes
     */
    public static byte[] zipString(String s) {
        if (s == null)
            return null;
        try {
            byte[] bytes = s.getBytes("GBK");
            BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(bytes));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(baos));
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
            return baos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解压String
     */
    public static String unzipString(byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return null;
        try {
            BufferedInputStream in = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
            bytes = baos.toByteArray();
            return new String(bytes, "GBK");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 从表名中分解出schmea名。
    public static String getSchemaFromFullTbName(String strName) {
        int index = strName.indexOf(".");
        if (index < 0)
            return null;
        return strName.substring(0, index);
    }

    public static Set<String> split(String strs, String delimeta){
        String[] segs = org.apache.commons.lang.StringUtils.splitByWholeSeparator(strs,delimeta);
        HashSet<String> ret = new HashSet<String>();
        for (int ii = 0; ii < segs.length; ii++){
            ret.add(segs[ii]);
        }
        return ret;
    }

    public static String getCurrentTime() {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.CHINA);
        return df.format(new Date());
    }

    @SuppressWarnings("unused")
    private static final boolean DEBUG = false;

    /**
     * 用于将字符串中的分隔符去掉重新组成字符串。
     * <p>
     * 例：<br>
     * String in = "this is a test";<br>
     * StringUtil.trimLine(in) returns "thisisatest"<br>
     * 
     * @param in 待处理的字符串
     * @return 处理后的字符串 <br>
     */
    public static String trimLine(String in) {
        StringTokenizer st = new StringTokenizer(in);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens())
            sb.append(st.nextToken());
        return sb.toString();
    }

    /**
     * 将字符串buf中的子串oldstr替换为newstr，返回新的字符串。
     * <p>
     * 例：<br>
     * String buf = "this is an test"; <br>
     * String oldstr = "an"; <br>
     * String newstr = "a"; <br>
     * StringUtil.replace(buf,oldstr,newstr) returns "this is a test" ; <br>
     * 注意：对单个字符的替换可以使用String的replace方法。 <br>
     * public String replace(char oldChar,char newChar) <br>
     * 例： <br>
     * "mesquite in your cellar".replace('e', 'o') returns "mosquito in your collar" <br>
     * 
     * @param buf 原字符串
     * @param oldstr 旧子串
     * @param newstr 新子串
     * @return 替换后的新字符串 <br>
     */

    /**
     * 用于返回指定字符串在字符串数组中首次出现的下标数。 <br>
     * <p>
     * 例： <br>
     * String str[] = {"abc","c1c","acc","cc"}; <br>
     * StringUtil.stringAt(str,"cc") returns 3 ;
     * 
     * @param strArray 字符串数组
     * @param item 字符串
     * @return 首次出现的下标值，如果没有，则返回-1 <br>
     */
    public static int stringAt(String[] strArray, String item) {
        for (int i = 0; i < strArray.length; i++)
            if (item.equals(strArray[i]))
                return i;
        return -1;
    }

    /**
     * 获得一个字符串数组的子数组, 返回数据的长度为指定(length)长度, 不足被空串("")
     * 
     * @param srcStrs 原字符串数组
     * @param start 子数组起始位置, 如小于0, 则以默认0作为起始位置
     * @param length 需要获得子数组的长度
     * @return add by shicy 2009-4-9
     */
    public static String[] subStrings(String[] srcStrs, int start, int length) {
        if (srcStrs == null)
            return new String[length];
        if (start < 0) // 小于0输入是非法的, 改为默认值0
            start = 0;
        String[] destStrs = new String[length];
        for (int i = 0; i < length; i++) {
            if (start > srcStrs.length)
                break;
            destStrs[i] = (String) srcStrs[start++];
        }
        return destStrs;
    }

    /**
     * 获得一个数组的子数组, 返回数据的长度为指定(length)长度, 不足被空串("")
     * 
     * @param srcStrs 原数组
     * @param start 子数组起始位置, 如小于0, 则以默认0作为起始位置
     * @param length 需要获得子数组的长度
     * @return add by shicy 2009-4-9
     */
    public static Object[] subArrays(Object[] srcStrs, int start, int length) {
        if (srcStrs == null)
            return new Object[length];
        if (start < 0) // 小于0输入是非法的, 改为默认值0
            start = 0;
        Object[] destStrs = new Object[length];
        for (int i = 0; i < length; i++) {
            if (start > srcStrs.length)
                break;
            destStrs[i] = srcStrs[start++];
        }
        return destStrs;
    }

   /**
     * 用于将两个排序后的二维数组按指定规则排序并合并成一个二维数组。
     * 
     * @param arr1[][] 二维数组1
     * @param arr2[][] 二维数组2
     * @param int 排序的列的下标值
     * @param kind 类型，提供3种排序类型（String,int,float）
     * @param asc 以升序或降序排列（true为升序）
     * @return 合并后的二维数组
     */
    public static String[][] sortTwoString(String[][] arr1, String[][] arr2, int number, String kind, boolean asc) {
        int len1 = 0;
        int len2 = 0;
        int list = 0;
        if (arr1 == null)
            return arr2;
        if (arr2 == null)
            return arr1;
        list = arr1.length;
        if (list != arr2.length || list - 1 < number || number < 0)
            return null;
        len1 = arr1[0].length;
        len2 = arr2[0].length;
        int i = 0, j = 0, k = 0;
        String[][] result = new String[list][len1 + len2];
        boolean side = true; //
        for (k = 0; k < len1 + len2;) {
            boolean compare = false;
            if (kind.equals("String"))
                compare = (arr1[number][i].toLowerCase().compareTo(arr2[number][j].toLowerCase()) < 0);
            else if (kind.equals("int"))
                compare = sToi(arr1[number][i], -1) < sToi(arr2[number][j], -1);
            else if (kind.equals("float"))
                compare = sTof(arr1[number][i]) < sTof(arr2[number][j]);
            if ((asc && compare) || ((!asc) && (!compare))) {
                result[number][k] = arr1[number][i];
                if (side)
                    side = false;
                int loop = 0;
                for (loop = 0; loop < number; loop++)
                    result[loop][k] = arr1[loop][i];
                for (loop = number + 1; loop < list; loop++)
                    result[loop][k] = arr1[loop][i];
                i++;
            }
            else {
                result[number][k] = arr2[number][j];
                if (!side)
                    side = true;
                int loop = 0;
                for (loop = 0; loop < number; loop++)
                    result[loop][k] = arr2[loop][j];
                for (loop = number + 1; loop < list; loop++)
                    result[loop][k] = arr2[loop][j];
                j++;
            }
            k++;
            if (i >= len1) {
                for (; j < len2; j++, k++)
                    for (int loop = 0; loop < list; loop++)
                        result[loop][k] = arr2[loop][j];
            }
            if (j >= len2 && i < len1) {
                for (; i < len1; i++, k++)
                    for (int loop = 0; loop < list; loop++)
                        result[loop][k] = arr1[loop][i];
            }
        }
        return result;
    }

    /**
     * 将字符串转换为浮点数
     * 
     * @param s 待转换的字符串
     * @return 浮点数 <br>
     */
    private static float sTof(String s) {
        float i = 0;
        if (s == null)
            return i;
        try {
            i = Float.valueOf(s).floatValue();
        }
        catch (NumberFormatException nfe) {
        }
        return i;
    }

    /**
     * 将字符串转换为数字 <br>
     * 
     * @param s 待转换的字符串
     * @param i 参数i
     * @return 整数 <br>
     */
    private static int sToi(String s, int i) {
        if (s == null)
            return i;
        try {
            i = Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
        }
        return i;
    }

    public static int count(String s, char c) {
        int n = 0;
        for(int pos = 0; (pos = s.indexOf(c, pos)) != -1; pos++)
            n++;
    
        return n;

    }

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 此处不能删，别处用到了

    public static String getDataString(Date date) {
        if (date == null)
            return "无";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
        sdf.applyPattern(PATTERN);
        return sdf.format(date);
    }

    /*
     * public static final String PATTERN2 = "yyyy-MM-dd hh:mm:ss.fffffffff"; public static final
     * java.text.SimpleDateFormat sdfDetail = new java.text.SimpleDateFormat(); static {
     * sdfDetail.applyPattern(PATTERN2); };
     * 
     * public static String getDataDetailString(Date date){ if(date == null) return "无"; return sdfDetail.format(date); }
     */

    /**
     * 判断是否为时间类类型
     * 
     * @param 输入的类型为：type date,timestamp,time等。
     * @return
     */
    public final static boolean isTimeType(String type) {
        if ((type.toLowerCase().indexOf("date") >= 0 || type.toLowerCase().indexOf("timestamp") >= 0 || type
                .toLowerCase().indexOf("time") >= 0))
            return true;
        return false;
    }
    
    public static boolean strToNumEquals(String codeValue, Object code)
    {
        if (codeValue.equals(code.toString()))
        {
            return true;
        }
        if (isNumeric(codeValue.trim()) && isNumeric(code.toString().trim()))
        {
            return sToi(codeValue.trim(), -1) == sToi(code.toString().trim(), -1);
        }
        return false;
    }

    /**
     * 是否数字
     * 
     * @param str
     * @return
     */
    static Pattern num_pattern = Pattern.compile("[0-9]*");

    public static boolean isNumeric(String str) {
        Matcher isNum = num_pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件扩展名
     * 
     * @param fileName
     * @return
     */
    public static String getFileType(String fileFullName) {
        String[] rets = org.apache.commons.lang.StringUtils.splitByWholeSeparator(fileFullName, ".");
        if (rets.length < 2)
            return null;
        return rets[rets.length - 1];
    }

    public static String getFileName(String fileFullName) {
        String[] rets = org.apache.commons.lang.StringUtils.splitByWholeSeparator(fileFullName, ".");
        StringBuffer buf = new StringBuffer();
        if (rets.length < 2)
            return null;

        for (int ii = 0; ii < rets.length - 1; ii++) {
            if (ii > 0)
                buf.append(".");
            buf.append(rets[ii]);
        }
        return buf.toString();
    }

    public final static String addSchema(String field, String table, String schema) {

        if (schema == null || schema.trim().equals("") || field == null || field.trim().equals(""))
            return field;

        if (table == null || table.trim().equals("")) {
            String[] sTemp;
            sTemp = org.apache.commons.lang.StringUtils.split(field, ".");
            if (sTemp.length == 3) {
                return field;
            }
            return schema + '.' + field;

        }
        int i;
        i = org.apache.commons.lang.StringUtils.lastIndexOf(field, ".");
        if (i >= 0) {
            field = field.substring(i + 1);
        }
        return schema + "." + table + "." + field;
    }

    /**
     * 
     * @param arg_node 传入的任务文件节点，定位于Step元素
     * @param strSource 需要处理Schema的字符串
     * @param isAdd 标志参数，为true则增加schema，false则去除schema
     * @return 处理后的字段域
     * @author niezg
     */
    public static String operateSchema(Node arg_node, String strSource, boolean isAdd) {
        boolean bFunction = false;
        // 检测字符串中是否有'('号，如果有则当作有函数的情况处理，可能也包括(filed)这种情况，但函数也保证这种情况下不会出错
        if (org.apache.commons.lang.StringUtils.indexOf(strSource, "(") >= 0)
            bFunction = true;
        // 不带有函数的情况，则只可能带有一个字段域
        String[] s_temp;
        String Schema = "";
        String strRet = "";
        Node tempNode;
        if (!bFunction) {
            s_temp = org.apache.commons.lang.StringUtils.split(strSource, ".");
            // 增加schema的情况
            if (isAdd) {
                if (s_temp.length == 3 || s_temp.length == 1) {
                    strRet = strSource;
                }
                else {
                    tempNode = arg_node.selectSingleNode("DataObjects/Table[@TablePhyName='" + s_temp[0] + "']");
                    if (tempNode == null) {
                        strRet = strSource;
                        return strRet;
                    }
                    Schema = tempNode.valueOf("@TableOwner");
                    strRet = Schema + "." + strSource;
                }
            }
            else {
                if (s_temp.length == 2 || s_temp.length == 1) {
                    strRet = strSource;
                }
                else {
                    strRet = s_temp[s_temp.length - 2] + "." + s_temp[s_temp.length - 1];
                }
            }
        }
        // 对带有函数的字符串进行处理
        else {
            boolean bFlag = true;
            int iTemp1, iTemp2, iTemp3;
            String ss = strSource;
            List<String> aListTemp = new ArrayList<String>(); // 保存拆分的函数字符的临时变量
            do {
                bFlag = false;
                iTemp1 = org.apache.commons.lang.StringUtils.indexOf(ss, "(");
                iTemp2 = org.apache.commons.lang.StringUtils.indexOf(ss, ",");
                iTemp3 = org.apache.commons.lang.StringUtils.indexOf(ss, ")");
                if (iTemp1 < 0 && iTemp2 < 0 && iTemp3 < 0) {
                    aListTemp.add(ss);
                }
                else {
                    bFlag = true;
                    if (iTemp1 >= 0
                            && (iTemp1 < ((iTemp2 >= 0 && iTemp3 >= 0) ? Math.min(iTemp2, iTemp3)
                                    : (iTemp2 < 0 && iTemp3 < 0) ? Integer.MAX_VALUE : Math.max(iTemp2, iTemp3)))) {
                        aListTemp.add(ss.substring(0, iTemp1 + 1).trim());
                        ss = ss.substring(iTemp1 + 1);
                    }
                    if (iTemp2 >= 0
                            && (iTemp2 < ((iTemp1 >= 0 && iTemp3 >= 0) ? Math.min(iTemp1, iTemp3)
                                    : (iTemp1 < 0 && iTemp3 < 0) ? Integer.MAX_VALUE : Math.max(iTemp1, iTemp3)))) {
                        if (iTemp2 != 0) {
                            aListTemp.add(ss.substring(0, iTemp2).trim());
                        }
                        aListTemp.add(",");
                        ss = ss.substring(iTemp2 + 1);
                    }
                    if (iTemp3 >= 0
                            && (iTemp3 < ((iTemp1 >= 0 && iTemp2 >= 0) ? Math.min(iTemp1, iTemp2)
                                    : (iTemp1 < 0 && iTemp2 < 0) ? Integer.MAX_VALUE : Math.max(iTemp1, iTemp2)))) {
                        if (iTemp3 != 0) {
                            aListTemp.add(ss.substring(0, iTemp3).trim());
                        }
                        aListTemp.add(")");
                        ss = ss.substring(iTemp3 + 1);
                    }
                }
                if (ss == null || ss.trim().equals(""))
                    bFlag = false;
            } while (bFlag);
            String sTemp;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < aListTemp.size(); i++) {
                sTemp = aListTemp.get(i);
                if (sTemp == null || sTemp.trim().equals(""))
                    continue;

                if (org.apache.commons.lang.StringUtils.indexOf(sTemp, ".") >= 0) {
                    s_temp = org.apache.commons.lang.StringUtils.split(sTemp, ".");
                    if (isAdd) {
                        if (s_temp.length == 2) {
                            tempNode = arg_node
                                    .selectSingleNode("DataObjects/Table[@TablePhyName='" + s_temp[0] + "']");
                            if (tempNode != null) {
                                Schema = tempNode.valueOf("@TableOwner");
                                sTemp = Schema + "." + sTemp;
                            }
                        }
                    }
                    else {
                        if (s_temp.length == 3) {
                            sTemp = s_temp[s_temp.length - 2] + "." + s_temp[s_temp.length - 1];
                        }
                    }
                }
                sb.append(sTemp);
            }
            strRet = sb.toString();
        }
        return strRet;
    }

    /**
     * 得到唯一的标式字符串
     * 
     * @return String
     */

    public static String getGuid() {
        long a = System.currentTimeMillis();
        java.util.Random r = new java.util.Random();
        int b = r.nextInt(100);
        return String.valueOf(a) + String.valueOf(b);
    }

    /**
     * 根据传入的文件类，获取该文件类的创建时间
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public static Timestamp getFileCreateTime(File file) throws Exception {
        Timestamp createdDate;
        String osName = System.getProperty("os.name");// 获取操作系统名称
        // 因为跨平台的原因，java不能获取到文件的创建时间，只有才用以下方式获取windows的创建时间，其他操作系统平台默认取修改时间
        if (osName.toLowerCase().indexOf("windows") >= 0) { // 是windows操作系统
            BufferedReader buffReader = null;
            try {
                Process ls_proc = Runtime.getRuntime().exec("cmd.exe /c dir \"" + file.getAbsolutePath() + "\" /tc");
                InputStreamReader in = new InputStreamReader(ls_proc.getInputStream());
                buffReader = new BufferedReader(in);
                for (int i = 0; i < 5; i++) {
                    buffReader.readLine();
                }
                String stuff = buffReader.readLine();
                StringTokenizer st = new StringTokenizer(stuff);
                String date = st.nextToken();
                String time = st.nextToken();
                // win7及2008的日期分隔符为/
                date = StringUtils.replace(date, "/", "-");
                // 获取出来的时间只到分钟，修改下格式
                if (org.apache.commons.lang.StringUtils.split(time, ":").length < 3)
                    time += ":00";
                String datetime = date + " " + time;
                createdDate = Timestamp.valueOf(datetime);
            }
            catch (Exception e) {
                return new Timestamp(0);// add by zyz,因为上面的stuff可能为空。
            }
            finally {
                try {
                    if (buffReader != null)
                        buffReader.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            createdDate = new Timestamp(file.lastModified());
        }
        return createdDate;
    }

    /**
     * 替换传入的字段域的table和schema
     * 
     * @param sourField 要替换的源域
     * @param sourTable 源表名
     * @param distTable 目标表名
     * @param sourSchema 源schema
     * @param distSchema 目标schema
     * @return 如果未做替换返回""，否则返回替换后的域
     */
    public static String replaceTabOfField(String sourField, String sourTable, String distTable, String sourSchema,
            String distSchema) {
        boolean bFunction = false;
        // 检测字符串中是否有'('号，如果有则当作有函数的情况处理，可能也包括(filed)这种情况，但函数也保证这种情况下不会出错
        if (org.apache.commons.lang.StringUtils.indexOf(sourField, "(") >= 0)
            bFunction = true;
        // 不带有函数的情况，则只可能带有一个字段域
        String[] s_temp;
        StringBuilder strRet = new StringBuilder(""); // 返回值
        if (!bFunction) {
            s_temp = org.apache.commons.lang.StringUtils.split(sourField, ".");
            if (s_temp.length == 2) { // 只带表名
                if (s_temp[0].equals(sourTable))
                    strRet.append(distTable).append(".").append(s_temp[1]);
            }
            else if (s_temp.length == 3) { // 带schema及表名
                if (s_temp[0].equals(sourSchema) && s_temp[1].equals(sourTable)) {
                    strRet.append(distSchema).append(".").append(distTable).append(".").append(s_temp[2]);
                }
            }
        }
        // 对带有函数的字符串进行处理
        else {
            boolean bFlag = true;
            int iTemp1, iTemp2, iTemp3;
            String ss = sourField;
            ArrayList<String> aListTemp = new ArrayList<String>(); // 保存拆分的函数字符的临时变量
            do {
                bFlag = false;
                iTemp1 = org.apache.commons.lang.StringUtils.indexOf(ss, "(");
                iTemp2 = org.apache.commons.lang.StringUtils.indexOf(ss, ",");
                iTemp3 = org.apache.commons.lang.StringUtils.indexOf(ss, ")");
                if (iTemp1 < 0 && iTemp2 < 0 && iTemp3 < 0) {
                    aListTemp.add(ss);
                }
                else {
                    bFlag = true;
                    if (iTemp1 >= 0
                            && (iTemp1 < ((iTemp2 >= 0 && iTemp3 >= 0) ? Math.min(iTemp2, iTemp3)
                                    : (iTemp2 < 0 && iTemp3 < 0) ? Integer.MAX_VALUE : Math.max(iTemp2, iTemp3)))) {
                        aListTemp.add(ss.substring(0, iTemp1 + 1).trim());
                        ss = ss.substring(iTemp1 + 1);
                    }
                    if (iTemp2 >= 0
                            && (iTemp2 < ((iTemp1 >= 0 && iTemp3 >= 0) ? Math.min(iTemp1, iTemp3)
                                    : (iTemp1 < 0 && iTemp3 < 0) ? Integer.MAX_VALUE : Math.max(iTemp1, iTemp3)))) {
                        if (iTemp2 != 0) {
                            aListTemp.add(ss.substring(0, iTemp2).trim());
                        }
                        aListTemp.add(",");
                        ss = ss.substring(iTemp2 + 1);
                    }
                    if (iTemp3 >= 0
                            && (iTemp3 < ((iTemp1 >= 0 && iTemp2 >= 0) ? Math.min(iTemp1, iTemp2)
                                    : (iTemp1 < 0 && iTemp2 < 0) ? Integer.MAX_VALUE : Math.max(iTemp1, iTemp2)))) {
                        if (iTemp3 != 0) {
                            aListTemp.add(ss.substring(0, iTemp3).trim());
                        }
                        aListTemp.add(")");
                        ss = ss.substring(iTemp3 + 1);
                    }
                }
                if (ss == null || ss.trim().equals(""))
                    bFlag = false;
            } while (bFlag);
            String sTemp;
            StringBuilder sb = new StringBuilder("");
            bFlag = false;// 初始化
            for (int i = 0; i < aListTemp.size(); i++) {
                sTemp = aListTemp.get(i);
                if (sTemp == null || sTemp.trim().equals(""))
                    continue;

                if (org.apache.commons.lang.StringUtils.indexOf(sTemp, ".") >= 0) {
                    s_temp = org.apache.commons.lang.StringUtils.split(sTemp, ".");
                    if (s_temp.length == 2) {
                        if (s_temp[0].equals(sourTable)) {
                            sTemp = distTable + "." + s_temp[1];
                            bFlag = true;
                        }
                    }
                    else if (s_temp.length == 3) {
                        if (s_temp[0].equals(sourSchema) && s_temp[1].equals(sourTable)) {
                            sTemp = distSchema + "." + distTable + "." + s_temp[2];
                            bFlag = true;
                        }
                    }
                }
                sb.append(sTemp);
            }
            if (bFlag)
                strRet = sb;
        }
        return strRet.toString();
    }

    /**
     * 工具函数，获取分割数据
     * 
     * @param lineData 要分割的行数据
     * @param separateChar 分割符
     * @param fieldEnclosedChar 包围符
     * @return
     */
    public static String[] getSeparateData(String lineData, String separateChar, String fieldEnclosedChar) {
        if (org.apache.commons.lang.StringUtils.isBlank(lineData))
            return null;// 如果传入行数据为空，则直接返回空
        separateChar = org.apache.commons.lang.StringUtils.trimToEmpty(separateChar);
        fieldEnclosedChar = org.apache.commons.lang.StringUtils.trimToEmpty(fieldEnclosedChar);
        String se = null;
        String defaultSeparateChar = ","; // 默认分割符
        // 组装分隔符
        if (org.apache.commons.lang.StringUtils.isEmpty(fieldEnclosedChar)) {
            if (org.apache.commons.lang.StringUtils.isEmpty(separateChar))
                se = defaultSeparateChar;
            else
                se = separateChar;
        }
        else {
            if (org.apache.commons.lang.StringUtils.isEmpty(separateChar))
                se = fieldEnclosedChar + defaultSeparateChar + fieldEnclosedChar;
            else
                se = fieldEnclosedChar + separateChar + fieldEnclosedChar;
        }
        String[] strArr = org.apache.commons.lang.StringUtils.split(lineData, se);// new String[stringTokenizer.countTokens()
                                                // +1];//StringUtils.splitByWholeSeparator(lineData, se, 100);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(fieldEnclosedChar)) {
            String temp = null;
            // 修改第一个格式字符串中的起始包围符号
            temp = strArr[0];
            if (temp.startsWith(fieldEnclosedChar))
                temp = temp.substring(fieldEnclosedChar.length());
            strArr[0] = temp;
            // 修改最后一个格式字符串中的结尾包围符号
            temp = strArr[strArr.length - 1];
            if (temp.endsWith(fieldEnclosedChar))
                temp = temp.substring(0, temp.length() - fieldEnclosedChar.length());
            strArr[strArr.length - 1] = temp;
        }
        return strArr;
    }

    /**
     * 把一个整形转换成字符串，但确保一定的长度，长度不足时在前面补0
     * 
     * @param value 待转换值
     * @param length 转换后的长度
     * @return
     */
    public static String intToStrByLength(int value, int length) {
        String ret = Integer.toString(value);
        if (ret.length() >= length)// 已经超出
            return ret;
        for (int ii = ret.length(); ii < length; ii++)
            ret = '0' + ret;
        return ret;
    }

    /**
     * 模拟apache高版本 StringUtils的endsWith，低版本中没有，此处提供
     * 
     * @param str
     * @param suffix
     * @param ignoreCase 是否忽略大小写，默认false
     * @return
     */
    public static boolean endsWith(String str, String suffix, boolean ignoreCase) {
        if (str == null || suffix == null)
            return str == null && suffix == null;
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(ignoreCase, strOffset, suffix, 0, suffix.length());
    }

    /**
     * MyBatis sqlCommondType: UNKNOWN = new SqlCommandType("UNKNOWN", 0); INSERT = new SqlCommandType("INSERT", 1);
     * UPDATE = new SqlCommandType("UPDATE", 2); DELETE = new SqlCommandType("DELETE", 3); SELECT = new
     * SqlCommandType("SELECT", 4);
     */
    private static String[] SQLKEYS = new String[] { "insert into ", "update ", " from " };

    /**
     * 从sql语句中扣取的数据库表名和方案名，只针对简单的sql语句，没有完善的解决方案
     * 
     * @param sql
     * @return 0:schema name;1:tableName; 若解析失败返回null
     * @author ibm created on 2012-2-20
     * @since SHK Framework 1.0
     */
    public static String[] parseTableFromSql(String sql) {
        String token = "";// SQLKEYS[mappedStatement.getSqlCommandType().ordinal()];
        // if (token.length() <= 0)
        // return null;

        sql = sql.trim();
        int index = -1;
        for (int ii = 0; ii < SQLKEYS.length; ii++) {
            index = org.apache.commons.lang.StringUtils.indexOfIgnoreCase(sql, SQLKEYS[ii]);
            if (index >= 0) {
                token = SQLKEYS[ii];
                break;
            }
        }
        // index = StringUtils.indexOfIgnoreCase(sql, token);//int index = sql.indexOf(token);
        if (index < 0)
            return null;

        int beginIndex = index + token.length();
        int end = sql.indexOf(" ", beginIndex);
        if (end < 0)
            end = sql.length();
        String tableName = sql.substring(beginIndex, end);
        if (tableName != null && tableName.length() > 0)
            tableName = tableName.trim();
        String[] schTable = org.apache.commons.lang.StringUtils.splitByWholeSeparator(tableName, ".");
        return schTable;
    }

    public static void main(String args[]) {
        String string = "127.0.0.1&tonnyshen&hello&&";
        String s[] = org.apache.commons.lang.StringUtils.split(string, '&');
        for (int i = 0; i < s.length; i++)
            System.out.println(">>>>" + s[i]);
        
        System.out.println(isNumeric(" 202 ".trim()));
        System.out.println(isNumeric(" s "));
        System.out.println(sToi(" 2 ".trim(), -1));
        System.out.println(sToi(" sb ", -1));
    }
}
