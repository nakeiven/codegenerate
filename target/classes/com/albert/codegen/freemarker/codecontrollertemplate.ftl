<#include "common.ftl">
/*
 * 文件名称: ${_cname}Controller.java
 * 版权信息: Copyright 2001-${_year} ${_company}. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: ${_uname}
 * 修改日期: ${_time}
 * 修改内容: 
 */
package ${_controllpck};

import ${_cfullname};
import ${_servicefullname};
import ${_daofullname};
import java.util.List;
import com.manfen.bizfdn.frm.web.MfhController;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ${_description}控制器入口
 * http://localhost:9080/../${_clname}?needXml=true
 * @author ${_uname} created on ${_time}
 * @since 
 */
@Controller
@RequestMapping(value = "/${_clname}")
public class ${_viewcontrollname} extends MfhController<${obj.pkJavaType}, ${_cname}, ${_daoname}, ${_servicename}>{
    
	@Autowired
    private ${_servicename} service;
    
    @Override
    protected ${_servicename} getService() {
        return service;
    }    

    @Override
    protected Class<${_cname}> getEntityClass() {
        return ${_cname}.class;
    }

    @Override
    protected Type getEntityType() {
        return new TypeToken<${_cname}>(){}.getType();
    }

    @Override
    protected Type getEntityListType() {
        return new TypeToken<List<${_cname}>>(){}.getType();
    }
}
