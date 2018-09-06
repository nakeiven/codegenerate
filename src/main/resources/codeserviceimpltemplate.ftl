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
import com.shk.common.app.service.impl.ComnService;
import com.shk.common.app.domain.ViewProperty;
import com.dway.webdbform.bean.common.WriteFieldBase;
import com.shk.common.app.ViewIndex;
import com.dins.comn.logic.SeqInit.SeqArea;
import java.util.List;
import com.shk.common.app.service.impl.SaasServiceFilter;
import com.shk.common.app.service.IServiceFilter;
<#if _haveFileField = "1">
import com.dins.comn.bean.Pair;
import com.shk.common.app.service.impl.FileAttachServiceFilter;
import com.shk.common.app.service.IFileSupportService;
import java.io.InputStream;
import com.dins.comn.fs.FileParam;
import com.dway.webdbform.bean.primarybean.Vwruls;
</#if>
<#if _haveMixField = "1">
import com.shk.common.app.service.impl.FileMixServiceFilter;
import com.shk.common.app.service.IMixFileService;
import com.dins.comn.fs.FileAbleField;
</#if>

/**
 * ${_description}服务实现类
 * @author ${_uname} created on ${_time}
 * @since 
 */
public class ${_servicename} extends ComnService implements ${_servicename} <#if _haveFileField = "1">,IFileSupportService</#if> <#if _haveMixField = "1">,IMixFileService</#if> {
	private ${_daoname} dao;
	<#if _haveFileField = "1">
	private FileAttachServiceFilter fileFilter = null;
	</#if>	
	<#if _haveMixField = "1">
	private FileMixServiceFilter mixFilter = null;
	</#if>
    public final static String VIEW_${_cuname} = "${_clname}";
	
	public ${_serviceimplname}() {
        super();
        this.pojoClass = ${_cname}.class;
        this.tableName = "${_tablename}";
        this.description = "${_description}";
        ViewIndex.putService(VIEW_${_cuname}, this);        
		<#if _haveFileField = "1">
		fileFilter = new FileAttachServiceFilter(this, "${_attachFileFieldName}", "app.?.path");
		//fileFilter.addFileProp("fileFieldName2", "app.?.path");//可以添加多个文件字段
		</#if>			
		<#if _haveMixField = "1">		
		mixFilter = new FileMixServiceFilter(this, "${_mixFileFieldName}", "app.?.path", 4000);
		</#if>
    }    

    @Override
    protected void initServiceFilters(List<IServiceFilter> filters) {        
        filters.add(new SaasServiceFilter(this));
		<#if _haveFileField = "1">
		filters.add(fileFilter);
		</#if>       
		<#if _haveMixField = "1">
		filters.add(mixFilter);
		</#if>
    }
    
    <#if _haveFileField = "1">
    @Override
    public Pair<String, Pair<InputStream, Long>> getEntityFile(Object pkObj, 
            String propName, String fileName) {
        return fileFilter.getEntityFile(pkObj, propName, fileName);
    }

    @Override
    public String saveEntityFile(Object pkObj, String propName, FileParam param) {
        return fileFilter.saveEntityFile(pkObj, propName, param);
    }
    </#if>
    
    <#if _haveMixField = "1">
    @Override
    public void switchMixFileToContent(FileAbleField obj, String propName) {
        mixFilter.switchMixFileToContent(obj, propName);        
    }
    </#if>
    
    @Override
    public String getCacheDomainName() {
        return ${_daoname}.class.getName();
    }

    @Override
    protected SeqArea getSequenceArea() {
        return SeqArea.hundred;
    }
	
	/**
     * 查询一条${_description}记录
     * @param pkObj 对象主键，可能是复合对象
     * @since
     */
    @Override
	public ${_cname} getEntityById(Object pkObj) {
		return (${_cname})dao.getEntityById(pkObj);
	}
	
	// -------------------------------- 以下为Gettter/Setter方法 -------------------------------- //
	@Override
	public ${_daoname} getDao(){
		return dao;
	}

	public void setDao(${_daoname} dao){
		this.dao = dao;
	}
	
	// -------------------------------- 以下为表单配置 -------------------------------- //
		
    /**@Override
    protected void initQueryProps(Map<String, ViewProperty> queryPropMap, String viewId) {
        super.initQueryProps(queryPropMap, viewId); 
        ViewProperty prop;
        prop = new ViewProperty(new QueryQfiled(com.shk.glp.resource.common.Constants.VIEW_CHAPTER + ".bookId", "教材编号", "string"), null); 
        queryPropMap.put(prop.getField().getName(), prop);
        prop.getQueryField().setHidden("true");
    }*/
        
    @Override
    protected String[] collectQueryProps() {
        return new String[] {"${obj.primaryKey}"};
    }
    
    @Override
    protected void initPropMapInner(List<ViewProperty> props) {
    	//DomainDirectRef.getRef().addOption("name","code"); DomainCodeRef.getRef(Constants.CODE_TYPE, Constants.LEVEL_CODE);
        ViewProperty prop = null;        
        <#list obj.props as prop>
        
        <#if obj.primaryKey = prop.name>		
		prop = new ViewProperty(new WriteFieldBase("${prop.name}", "编号", "${prop.type}"), null);    
		<#else>
		<#if prop.name = _attachFileFieldName>
		WriteFieldBase field = new WriteFieldBase("${_attachFileFieldName}", "${_attachFileFieldName}", "${prop.type}", false);//必须是false，文件本身在临时变量里。
    	Vwruls vwruls = new Vwruls(com.dway.webdbform.tools.Constants.FILE);
        vwruls.setFormat("/res/downResFile.action?type=" + "1?");
        field.setVwruls(vwruls);
        prop = new ViewProperty(field, null);
		<#else>
		prop = new ViewProperty(new WriteFieldBase("${prop.name}", "${prop.caption}", "${prop.type}", false), null);
		</#if>
		</#if>
    	props.add(prop);
		</#list>
    }
}
