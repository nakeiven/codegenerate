/*
 * 文件名称: CollectionUtils.java
 * 版权信息: Copyright 2001-2011 ZheJiang Collaboration Data System Co., LTD. All right reserved.
 * -------------------------------------------------------------------------------------------
 * 修改历史:
 * -------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: LuoJingtian
 * 修改日期: 2011-07-15
 * 修改内容: 集合工具类
 * -------------------------------------------------------------------------------------------
 * 修改原因: 将数据交换工程的集合工具类移除, 内容合并到公共工程中
 * 修改人员: LuoJingtian
 * 修改日期: 2011-11-8
 * 修改内容: 集合工具类
 */
package com.albert.modules.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

/**
 * 集合工具类
 * 
 * @author <a href="mailto:luojt@zjcds.com">LuoJingtian</a> created on Jul 22, 2011
 * @since SHK Framework 1.0
 */
public final class CollectionUtils {
    public static StringForIntegerCompare intComparatorByString = new StringForIntegerCompare();
    /**
     * 把字符串作为整形来比较
     * 
     * @author zhangyz created on 2012-7-9
     * @since Framework 1.0
     */
    public static class StringForIntegerCompare implements Comparator<String>{
        
        public int compare(String arg0, String arg1) {
            int n1 = Integer.parseInt(arg0);
            int n2 = Integer.parseInt(arg1);            
            if (n1 < n2)
                return -1;
            else if (n1 == n2)
                return 0;
            else
                return 1;
        }
    }
    
    /**
     * 倒序比较器
     * 
     * @author zhangyz created on 2013-3-16
     * @since Framework 1.0
     */
    public static class IntReverse implements Comparator<Integer> {
       
        public int compare(Integer arg0, Integer arg1) {
            if (arg0 == arg1)
                return 0;
            else if (arg0.intValue() > arg1.intValue())
                return -1;
            else
                return 1;
        }
        
    }

    /** 默认构造函数, 防止实例化 */
    private CollectionUtils() {

    }

    /**
     * 判断集合是否为空
     * 
     * @param coll 集合类型, 包括List, Set等
     * @return true: 集合空; false: 集合非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 判断集合是否非空
     * 
     * @param coll 集合类型, 包括List, Set等
     * @return true: 集合非空; false: 集合空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断映射是否为空
     * 
     * @param map 映射类型
     * @return true: 映射空; false: 映射非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断映射是否非空
     * 
     * @param map 映射类型
     * @return true: 映射非空; false: 映射空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断对象数组是否为空
     * 
     * @param map 对象数组
     * @return true: 对象数组空; false: 对象数组非空
     */
    public static boolean isEmpty(Object[] objs) {
        return objs == null || objs.length == 0;
    }

    /**
     * 判断对象数组是否非空
     * 
     * @param objs 对象数组类型
     * @return true: 对象数组非空; false: 对象数组空
     */
    public static boolean isNotEmpty(Object[] objs) {
        return !isEmpty(objs);
    }

    /**
     * 判断基本类型数组是否为空.
     * 
     * @param obj 基本类型数组, 只能是数组类型
     * @return true: 基本类型数组空; false: 基本类型数组非空
     * @author LuoJingtian created on 2011-8-9
     * @since DE 5.2
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            if (cls.equals(int[].class)) {
                return ((int[]) obj).length == 0;
            }
            else if (cls.equals(long[].class)) {
                return ((long[]) obj).length == 0;
            }
            else if (cls.equals(byte[].class)) {
                return ((byte[]) obj).length == 0;
            }
            else if (cls.equals(short[].class)) {
                return ((short[]) obj).length == 0;
            }
            else if (cls.equals(float[].class)) {
                return ((float[]) obj).length == 0;
            }
            else if (cls.equals(double[].class)) {
                return ((double[]) obj).length == 0;
            }
            else if (cls.equals(boolean[].class)) {
                return ((boolean[]) obj).length == 0;
            }
            else if (cls.equals(char[].class)) {
                return ((char[]) obj).length == 0;
            }
        }

        // 对非数组类型, 默认是空的
        return true;
    }

    /**
     * 判断基本类型数组是否非空.
     * 
     * @param obj 基本类型数组, 只能是数组类型
     * @return true: 基本类型数组非空; false: 基本类型数组空
     * @author LuoJingtian created on 2011-8-9
     * @since DE 5.2
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 检测两个Map内容是否相等，不要求顺序一致
     * 
     * @param sourceMap 源Map
     * @param distMap 目标Map
     * @return true: 内容相等 false:内容不等
     * @author niezg created on 2009-3-29
     */
    public static boolean isEquals(Map<?, ?> sourceMap, Map<?, ?> distMap) {
        boolean result = false;
        Object sourObject = null, distObject = null;
        if (isEmpty(sourceMap) && isEmpty(distMap))
            result = true;
        else if (isNotEmpty(sourceMap) && isNotEmpty(distMap)) {
            if (sourceMap.size() == distMap.size()) {
                Set<?> keySet = sourceMap.keySet();
                result = true;
                for (Object ele : keySet) {
                    sourObject = sourceMap.get(ele);
                    distObject = distMap.get(ele);
                    if (distObject == null || !distObject.equals(sourObject)) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 比较两个数组内容是否完全一样，不要求顺序一致
     * 
     * @param array1 源数组
     * @param array2 目标数组
     * @return added by niezg 2009-3-29
     */
    public static boolean isUnorderedEquals(Object[] array1, Object[] array2) {
        boolean result = true;
        if (array1 != array2) {
            if (array1 == null || array2 == null)
                result = false;
            else {
                if (array1.length != array2.length) {
                    result = false;
                }
                else {
                    if (array1.length != 0) {
                        for (Object obj : array1) {
                            if (!ArrayUtils.contains(array2, obj)) {
                                result = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 比较两个list内容是否完全一样，不要求顺序一致
     * 
     * @param list1 源List
     * @param list2 目标List
     * @return added by niezg 2009-3-29
     */
    public static boolean isUnorderedEquals(List<?> list1, List<?> list2) {
        boolean result = true;
        if (list1 != list2) {
            if (list1 == null || list2 == null)
                result = false;
            else {
                Object[] objArr1 = list1.toArray();
                Object[] objArr2 = list2.toArray();
                result = isUnorderedEquals(objArr1, objArr2);
            }
        }
        return result;
    }
}
