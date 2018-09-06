<#include "common.ftl">
/*
 * 文件名称: ${_cname}ServiceImpl.java
 * 版权信息: Copyright 2001-${_year} ${_company}. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: ${_uname}
 * 修改日期: ${_time}
 * 修改内容: 
 */
package ${_servicepck};

import ${_daofullname};
import ${_cfullname};
import ${_servicefullname};
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
<#if _haveFileField = "1">
import com.mfh.comn.bean.Pair;
import com.manfen.modules.app.service.impl.FileAttachServiceFilter;
import com.manfen.modules.app.service.IFileSupportService;
import java.io.InputStream;
import com.mfh.comn.fs.FileParam;
import com.manfen.modules.app.wfm.bean.Vwruls;
</#if>
<#if _haveMixField = "1">
import com.manfen.modules.app.service.impl.FileMixServiceFilter;
import com.manfen.modules.app.service.IMixFileService;
import com.mfh.comn.fs.FileAbleField;
</#if>
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ${_description}服务实现类
 * @author ${_uname} created on ${_time}
 * @since 
 */
@Component
public class ${_servicename}  {
	@Autowired
	private ${_daoname} dao;
	
	private final static String TABLE_NAME = "${_tablename}";
    //public final static String DAO_EVENT_KEY = MsgNotifyService.DAO_EVENT_PRE + TABLE_NAME;	
	<#if _haveFileField = "1">
	private FileAttachServiceFilter<${_cname}, ${obj.pkJavaType}> fileFilter = null;
	</#if>	
	<#if _haveMixField = "1">
	private FileMixServiceFilter<${_cname}, ${obj.pkJavaType}> mixFilter = null;
	</#if>
    public final static String VIEW_${_cuname} = "${_clname}";
	
	/**
     * 构造函数
     */
	public ${_servicename}() {
        super();
        <#if _haveFileField = "1">
		fileFilter = new FileAttachServiceFilter<${_cname}, ${obj.pkJavaType}>(this, "${_attachFileFieldName}", "app.?.path");
		//fileFilter.addFileProp("fileFieldName2", "app.?.path");//可以添加多个文件字段
		</#if>			
		<#if _haveMixField = "1">		
		mixFilter = new FileMixServiceFilter<${_cname}, ${obj.pkJavaType}>(this, "${_mixFileFieldName}", "app.?.path", 4000);
		</#if>
    }  
    
    public ${_daoname} getDao() {
        return dao;
    }
    
    <#if _haveBizkeys = '1'>    
    @Override
    public void create(${_cname} ${_clname}) {
        if (!this.isExistByBizKeys(<#list obj.bizKeys as prop><#if (prop_index>=1)>, </#if>${_clname}.get${prop.name?cap_first}()</#list>)) {
            super.create(${_clname});
        }        
        else
            throw new RuntimeException("指定的信息已经存在!");
    }
    
    public ${_cname} getByBizKeys(<#list obj.bizKeys as prop><#if (prop_index>=1)>, </#if>${prop.type} ${prop.name}</#list>) {
        return super.getByBizKeysInner(<#list obj.bizKeys as prop><#if (prop_index>=1)>, </#if>"${prop.name}", ${prop.name}</#list>);
    }
    
	@Transactional(rollbackFor = { java.lang.RuntimeException.class, java.lang.Exception.class })
    public void updateByBizKeys(${_cname} object) {
        super.updateByBizKeys(object);
    }
    
    @Transactional(rollbackFor = { java.lang.RuntimeException.class, java.lang.Exception.class })
    public void deleteByBizKeys(<#list obj.bizKeys as prop><#if (prop_index>=1)>, </#if>${prop.type} ${prop.name}</#list>) {
        ${_cname} bean = getByBizKeys(<#list obj.bizKeys as prop><#if (prop_index>=1)>, </#if>${prop.name}</#list>);
        if (bean != null)
        	delete(bean);
    }
    
    public boolean isExistByBizKeys(<#list obj.bizKeys as prop><#if (prop_index>=1)>, </#if>${prop.type} ${prop.name}</#list>) {
        return super.isExistByBizKeysInner(<#list obj.bizKeys as prop><#if (prop_index>=1)>, </#if>"${prop.name}", ${prop.name}</#list>);
    }
    
    </#if>
    
    
    <#if _haveFileField = "1">    
    @Override
    public Pair<String, Pair<InputStream, Long>> getEntityFile(${_cname} pkObj, 
            String propName, String fileName) {
        return fileFilter.getEntityFile(pkObj, propName, fileName);
    }

    @Override
    public String saveEntityFile(${obj.pkJavaType} pkObj, String propName, FileParam param) {
        return fileFilter.saveEntityFile(pkObj, propName, param);
    }
    
    </#if>    
    <#if _haveMixField = "1">    
    @Override
    public void switchMixFileToContent(FileAbleField obj, String propName) {
        mixFilter.switchMixFileToContent(obj, propName);        
    }
    
    </#if>    
	// -------------------------------- 以下为表单配置 -------------------------------- //
		
    /*@Override
    protected void initQueryProps(Map<String, ViewProperty<?>> queryPropMap, String viewId) {
        super.initQueryProps(queryPropMap, viewId); 
        ViewProperty prop;
        prop = new ViewProperty(new QueryQfiled(com.shk.glp.resource.common.Constants.VIEW_CHAPTER + ".bookId", "教材编号", "string"), null); 
        queryPropMap.put(prop.getField().getName(), prop);
        prop.getQueryField().setHidden("true");
    }*/
    /*    
    @Override
    protected String[] collectQueryProps() {
        return new String[] {"${obj.primaryKey}"};
    }*/
    
    /*@Override
    public List<String> collectSearchPropNames(String viewId) {
        List<String> propNames = new ArrayList<String>();     
        return propNames;
    }*/
    /*
    @Override
    protected void initPropMapInner(List<ViewProperty<?>> props) {
    	//DomainDirectRef.getRef().addOption("name","code"); DomainCodeRef.getRef(Constants.CODE_TYPE, Constants.LEVEL_CODE);
        ViewProperty<?> prop = null;        
        <#list obj.props as prop>
        
        <#if obj.primaryKey = prop.name>		
		prop = new ViewProperty<Object>(new WriteFieldBase("${prop.name}", "编号", "${prop.type}"), null);    
		<#else>
		<#if prop.name = _attachFileFieldName>
		WriteFieldBase field = new WriteFieldBase("${_attachFileFieldName}", "${_attachFileFieldName}", "${prop.type}", false);//必须是false，文件本身在临时变量里。
    	Vwruls vwruls = new Vwruls(com.manfen.modules.app.wfm.bean.Constants.FILE);
        vwruls.setFormat("/res/downResFile.action?type=" + "1?");
        field.setVwruls(vwruls);
        prop = new ViewProperty<Object>(field, null);
		<#else>
		prop = new ViewProperty<Object>(new WriteFieldBase("${prop.name}", "${prop.caption}", "${prop.type}", false), null);
		</#if>
		</#if>
    	props.add(prop);
		</#list>
    }*/
}
