<#include "common.ftl">
/*
 * 文件名称: ${_cname}ViewAction.java
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

import ${_servicefullname};
import com.shk.web.action.BaseQueryAction;
import com.shk.common.app.service.IComnService;
import com.shk.frm.core.ContextHolder;
import com.shk.frm.dao.QueryDefine;
import com.shk.frm.dao.paging.PageInfo;
import com.dway.common.ParentChildItem;
import com.shk.frm.core.ReturnInfo;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
<#if _haveMixField = "1">
import com.dins.comn.fs.FileAbleField;
import com.shk.common.app.service.IMixFileService;
</#if>

/**
 * ${_description}查询入口
 * http://localhost:9080/../query${_cname}.action?needXml=true
 * @author ${_uname} created on ${_time}
 * @since 
 */
@SuppressWarnings("serial")
public final class ${_viewqueryname} extends BaseQueryAction{
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
    protected List<ParentChildItem> doQueryCodeSubOptions(String hierId, String codeId, String levelName) {
        List<ParentChildItem> options = null;
        if (StringUtils.isBlank(codeId)){            
            //ParentService parentService = ContextHolder.getBean(ParentService.class);
            options = null;
        }
        else if (levelName != null){//逐级查询下级编码
            /*com.dway.common.code.UnionCode unCode = new com.dway.common.code.UnionCode(codeId);
            if (levelName.equals(Constants.LEVEL_CLASS)){
                String[] params = this.assertParam(unCode.getCodeInOneTable(0), 1);
                options = service.queryGroupsByClass(params[0]);
            }
            else if (levelName.equals(Constants.LEVEL_GROUP)){
                return null;
            }*/
        }
        return options;
    }

    @Override
    protected List<?> doQuery(IComnService service, QueryDefine define, 
    	PageInfo pageInfo, ReturnInfo retInfo) {
        List<?> ret = (List<?>) (service.getSimpleDao().searchEntities(define, pageInfo));
        <#if _haveMixField = "1">
        FileAbleField bean;
        IMixFileService mixService = (IMixFileService)service;
        for (int ii = 0; ii < ret.size(); ii++){
            bean = (FileAbleField)ret.get(ii);
            mixService.switchMixFileToContent(bean, "${_mixFileFieldName}");
        }
        </#if>
        return ret;
    }
}
