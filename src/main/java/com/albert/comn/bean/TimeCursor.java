/*
 * 文件名称: TimeCursor.java
 * 版权信息: Copyright 2013-2014 chunchen technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: zhangyz
 * 修改日期: 2014-3-20
 * 修改内容: 
 */
package com.albert.comn.bean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author administrator
 *
 * 时间戳类型，三种
 */
public class TimeCursor implements Cloneable{
    public static final String ORACLE_ROWID= "rowid";
    
    private short type;//时间戳类型
    private String strValue = null;//时间戳值--string
    
    private transient long lValue;//时间戳值--long，string等价值。
    private transient Timestamp tValue = null;//时间戳值-针对timestap

    //时间戳的种类，每种解决方案都需要用到游标类型;
    public static final short CRTYPE_DATE = 1;
    public static final short CRTYPE_SEQ = 2;
    public static final short CRTYPE_STR = 3;
    public static final short CRTYPE_ROWID = 4;
    public static final short CRTYPE_UNKNOWN = 5;
    public static final String CRTYPE_HEX_TAG = "0x";
    private static final String PAD = "00000000000000000000";//20个0
    public final static String XACT_SEQNO_INIT = CRTYPE_HEX_TAG + PAD; //初始游标
    
    //上述时间戳类型的初始值，原来设计为>这些值，后来>=这些值也可以。
    public static Timestamp CRTYPE_DATE_ZERO = new Timestamp(0);
    public static String CRTYPE_ROWID_ZERO = "0";
    public static long CRTYPE_SEQ_ZERO = -1;
    public static String CRTYPE_STR_ZERO = "!"; //ascii码为33
    
    //
    public static final String INNER_DATAFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String INNER_DATAFORMATSHORT = "yyyy-MM-dd";
    public static final String INNER_TIMEFORMAT = "HH:mm:ss";
    public static final String INER_TIME_FOR_CHAT = "yyyy/MM/dd HH:mm";
    
    public static final SimpleDateFormat TimeFormat = new SimpleDateFormat(INNER_TIMEFORMAT);//内部使用的日期格式。
    public static final SimpleDateFormat InnerFormatShort = new SimpleDateFormat(INNER_DATAFORMATSHORT);//内部使用的日期格式。    
    
    //注意线程不安全
    public static java.text.SimpleDateFormat InnerFormat = new SimpleDateFormat(INNER_DATAFORMAT);
    
    public static java.text.SimpleDateFormat InnerFormat(){
        return new SimpleDateFormat(INNER_DATAFORMAT);
    }
    
    public static java.text.SimpleDateFormat InnerFormatShort(){
        return new SimpleDateFormat(INNER_DATAFORMATSHORT);
    }
    
    public static java.text.SimpleDateFormat TimeFormat(){
        return new SimpleDateFormat(INNER_TIMEFORMAT);
    }
    
    public final static String[] flagDes = {"日期型","数值型","字符型","oracle行号","未知"};
    
    public TimeCursor(short type){
        this.type = type;
        reset();        
    }

    @Override
    public Object clone() {
        TimeCursor ret = new TimeCursor(type);
        ret.strValue = this.strValue;
        ret.lValue = this.lValue;
        if (this.tValue != null)
            ret.tValue = (Timestamp)this.tValue.clone();
        else
            ret.tValue = null;
        return ret;
    }
    
    /**
     * 将游标往后移动指定的个数
     * @param num
     * @author zhangyz created on 2013-6-24
     */
    public void moveNext(long num) {
        if(type == TimeCursor.CRTYPE_SEQ){
            lValue += num;            
        }
        else if (type == TimeCursor.CRTYPE_DATE) {
            long lValue = tValue.getTime() + num * 1000;//递增的是秒数,变成毫秒数
            tValue.setTime(lValue);
            strValue = tValue.toString();
        }
        else if (isHexSeq()) {
            lValue += num;
            strValue = Long.toHexString(lValue).toUpperCase();            
            if (strValue.length() < PAD.length()) {
                strValue = PAD.substring(0, PAD.length() - strValue.length()) + strValue;
            }
            strValue = CRTYPE_HEX_TAG + strValue; 
        }
        else
            throw new RuntimeException("不支持的游标类型!");
    }
    
    /**
     * 移动到指定的位置
     * @param pa_strValue
     * @author zhangyz created on 2013-6-24
     */
    private void moveToValue(String pa_strValue) {
        strValue = pa_strValue;//不能丢
        if(strValue == null){
            reset();
        }
        else{
            if(type == TimeCursor.CRTYPE_DATE) {
                try{
                    tValue = Timestamp.valueOf(strValue);
                }
                catch(IllegalArgumentException e){
                    tValue = CRTYPE_DATE_ZERO;
                    strValue=CRTYPE_DATE_ZERO.toString();
                    lValue=CRTYPE_DATE_ZERO.getTime();
                }
            }
            else if(type == TimeCursor.CRTYPE_SEQ)
                lValue = Long.parseLong(strValue);
            else if (isHexSeq()) {
                lValue = Long.parseLong(strValue.substring(CRTYPE_HEX_TAG.length()), 16);
            }
        }
    }
    
    /**
     * 是否十六进制数值类型的游标
     * @return
     * @author zhangyz created on 2013-7-30
     */
    public boolean isHexSeq() {
        return (type == TimeCursor.CRTYPE_STR && strValue.startsWith(CRTYPE_HEX_TAG));
    }
    
    public void setValue(short type, String pa_strValue){
        this.type = type;       
        moveToValue(pa_strValue);
    }
    
    public TimeCursor(short type, String pa_strValue){
        setValue(type, pa_strValue);
    }

    /**
     * 倘若结果集返回为空，游标置将被置成初始值
     * @param type 类型
     * @param rs 结果集及位置
     * @param pos
     * @throws SQLException
     */
    public TimeCursor(short type, ResultSet rs, int pos) throws SQLException{
        setValue(type, rs.getString(pos));//游标表中，如果是日期型，游标值也是字符串，内容是timestamp的字符串格式。
    }
    
    public TimeCursor(long pa_value) {
        type = TimeCursor.CRTYPE_SEQ;
        lValue = pa_value;  
    }
    
    public TimeCursor(String pa_value){
        type = TimeCursor.CRTYPE_STR;
        setValue(type, pa_value);
    }
    
    public TimeCursor(Timestamp pa_value){
        type = TimeCursor.CRTYPE_DATE;
        tValue = pa_value;
    }
    
    @Override
    public String toString() {
        return getValue();
    }
    
    /**
     * 获取游标的字符串值
     * @return
     * @author zhangyz created on 2014-6-5
     */
    public String getValue(){
        if(type == TimeCursor.CRTYPE_DATE)
            return tValue.toString();
        else if(type == TimeCursor.CRTYPE_SEQ)
            return Long.toString(lValue);
        else if((type == TimeCursor.CRTYPE_STR) || (type == TimeCursor.CRTYPE_ROWID))
            return strValue;
        else
            return null;
    }
    
    /**
     * 强制获取数值类游标的数值型值
     * @return
     * @author zhangyz created on 2013-6-27
     */
    public long getNumericValue () {
        if(type == TimeCursor.CRTYPE_DATE)
            return tValue.getTime();
        else if(type == TimeCursor.CRTYPE_SEQ)
            return lValue;
        else if (isHexSeq()) {
            lValue = Long.parseLong(strValue.substring(CRTYPE_HEX_TAG.length()), 16);
            return lValue;
        }
        throw new RuntimeException("该游标类型不支持该操作!");           
    }
    
    /**
     * 针对日期输出,INNER_DATAFORMAT格式。
     * @return
     */
    public String getValueForWfm(){
        if(type == TimeCursor.CRTYPE_DATE){
            java.util.Date date = new java.util.Date(tValue.getTime());
            return InnerFormat.format(date);
        }
        else
            return getValue();
    }
    
    public short getType() {
        return type;
    }
    
    /**
     * 判断传入的某个游标值(结果集)是否与当前值相同。
     * @param //rs 结果集
     * @param //pos 结果集中的位置
     * @return 相同 true；不同 false
     * @throws SQLException
     */
    /*public boolean bEquals(ResultSet rs,int pos) throws SQLException{
        if(type == CRTYPE_DATE){
            if(rs.getTimestamp(pos).getTime() == lValue);
                return true;
        }
        else if(type == CRTYPE_SEQ){
            if(rs.getLong(pos) == lValue)
                return true;
        }
        else if((type == CRTYPE_STR) || (type == CRTYPE_ROWID)){
            if(rs.getString(pos).equals(strValue))
                return true;
        }
        return false;
    }*/
    public boolean bEquals(TimeCursor other){
        if(other == null)
            return false;
        if((type == TimeCursor.CRTYPE_SEQ)){
            return (lValue == other.lValue);//<=,不是<
        }
        else if(type == TimeCursor.CRTYPE_DATE){
            return (tValue.getTime() == other.tValue.getTime());
        }
        else if((type == TimeCursor.CRTYPE_STR) || (type == TimeCursor.CRTYPE_ROWID)){
            return strValue.equals(other.strValue);
        }
        return false;
    }
    
    /**
     * 是否在指定游标值之前
     * @param other
     * @return
     * @author zhangyz created on 2014-6-5
     */
    public boolean before(TimeCursor other){
        if( type == TimeCursor.CRTYPE_SEQ){
            return (lValue <= other.lValue);//<=,不是<
        }
        else if(type == TimeCursor.CRTYPE_DATE){
            if( tValue.getTime() <= other.tValue.getTime())
                return true;
            else
                return false;
        }
        else if((type == TimeCursor.CRTYPE_STR) || (type == TimeCursor.CRTYPE_ROWID)){
            if(strValue.compareTo(other.strValue) <= 0)
                return true;
            else
                return false;
        }
        return false;
    }
    
    /**
     * 是否在指定游标值之后
     * @param other
     * @return
     * @author zhangyz created on 2014-6-5
     */
    public boolean after(TimeCursor other){
        if((type == TimeCursor.CRTYPE_SEQ)){
            return (lValue > other.lValue);//不能>=
        }
        else if(type == TimeCursor.CRTYPE_DATE){
            if(tValue.getTime() > other.tValue.getTime())
                return true;
            else
                return false;
        }
        else if((type == TimeCursor.CRTYPE_STR) || (type == TimeCursor.CRTYPE_ROWID)){
            if(strValue.compareTo(other.strValue) > 0)
                return true;
            else
                return false;
        }
        return false;
    }
    
    /**
     * 将游标值清零 
     * @author zhangyz created on 2014-6-5
     */
    public void reset(){
        if(type == TimeCursor.CRTYPE_DATE)
            tValue = TimeCursor.CRTYPE_DATE_ZERO;
        else if(type == TimeCursor.CRTYPE_SEQ)
            lValue = TimeCursor.CRTYPE_SEQ_ZERO;
        else if(type == TimeCursor.CRTYPE_STR) {
            strValue = TimeCursor.CRTYPE_STR_ZERO;
            lValue = TimeCursor.CRTYPE_SEQ_ZERO;//可能有hex string
        }
        else if(type == TimeCursor.CRTYPE_ROWID)
            strValue = TimeCursor.CRTYPE_ROWID_ZERO;
    }
    
    /**
     * 数据交换中用，设置增量抽取的初始值与以前的初始值有区别
     * 防止多次全量抽取
     */
    public void setCDCInitValue(){
        if(type == TimeCursor.CRTYPE_DATE)
            tValue = new Timestamp(1);
        else if(type == TimeCursor.CRTYPE_SEQ)
            lValue = 0;
        else if(type == TimeCursor.CRTYPE_STR)
            strValue = TimeCursor.CRTYPE_STR_ZERO; //ascii码值35
        else if(type == TimeCursor.CRTYPE_ROWID)
            strValue = "1";
    }
    
    /**
     * 判读该游标值是否出于初始状态。
     * @return
     */
    public boolean isInit(){
        if(type == TimeCursor.CRTYPE_DATE)
            return tValue.equals(TimeCursor.CRTYPE_DATE_ZERO);
        else if(type == TimeCursor.CRTYPE_SEQ)
            return lValue == TimeCursor.CRTYPE_SEQ_ZERO;
        else if(type == TimeCursor.CRTYPE_STR)
            return strValue.equals(TimeCursor.CRTYPE_STR_ZERO) || strValue.equals(TimeCursor.XACT_SEQNO_INIT);
        else if(type == TimeCursor.CRTYPE_ROWID)
            return strValue.equals(TimeCursor.CRTYPE_ROWID_ZERO);
        return true;
    }
    
    //设置preparestatement,全部是字符型，用于游标表和实例信息表等处
    public void setPrepStmt(PreparedStatement pt,int pos) throws SQLException{
        /*if(type == TSTYPE_DATE)
            pt.setTimestamp(pos,new Timestamp(lValue));
        else if(type == TSTYPE_SEQ)
            pt.setLong(pos,lValue);
        else if(type == TSTYPE_STR)
            pt.setString(pos,strValue);*/
        pt.setString(pos,getValue());
    }
    
    /**
     * 按照实际类型设置prepareStatement
     * @param pt
     * @param pos
     * @throws SQLException
     */
    public void setPrepStmtFact(PreparedStatement pt, int pos) throws SQLException{
        if(type == TimeCursor.CRTYPE_DATE)
            pt.setTimestamp(pos,tValue);
        else if(type == TimeCursor.CRTYPE_SEQ)
            pt.setLong(pos,lValue);
        else if((type == TimeCursor.CRTYPE_STR) || (type == TimeCursor.CRTYPE_ROWID))
            pt.setString(pos,strValue);
    }
}
