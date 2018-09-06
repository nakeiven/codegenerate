<#include "common.ftl">
/*
 * 文件名称: ${_cname}SaveAction.java
 * 版权信息: Copyright 2001-${_year} ${_company}. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: ${_uname}
 * 修改日期: ${_time}
 * 修改内容: 
 */
package ${_viewpck};

import ${_cfullname};
import ${_servicefullname};
import com.shk.web.action.BaseSaveAction;
import com.shk.common.app.service.IComnService;
import com.shk.frm.core.ContextHolder;

/**
 * ${_description}维护入口
 * /../save${_cname}.action
 * @author ${_uname} created on ${_time}
 * @since 
 */
@SuppressWarnings("serial")
public final class ${_viewsavename} extends BaseSaveAction{
    private ${_servicename} service = ContextHolder.getBean(${_servicename}.class);
    
    @Override
    protected IComnService getService(String viewId) {
        return service;
    }

    @Override
    protected String getValidateMenuName(){
        return null;
    }
    
    @Override
    protected Object insertObject(Object item) {
        ${_cname} ${_clname} = (${_cname})item;
        service.insertEntity(${_clname});
        return ${_clname}.getId();
    }

    @Override
    protected void updateObject(Object item) {
        ${_cname} ${_clname} = (${_cname})item;
        service.updateEntity(${_clname});       
    }
}
