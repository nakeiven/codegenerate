<#include "common.ftl">
package ${_pck};

import org.junit.Test;
import com.dins.comn.ActionType;
import ${_cfullname};
import com.shk.web.test.BaseActionTestCase;

/**
 * 针对${_description}服务端的测试客户端
 * @author ${_uname} created on ${_time}
 */
public class ${_cname}ActionTest extends BaseActionTestCase {

	public ${_cname}ActionTest() {
        super();
    }
    
    @Override
    protected String getActionName(ActionType actionType) {
        if (isQueryKinds(actionType))
            return "??/query${_cname}.action";
        else
            return "??/save${_cname}.action";
    }

    @Override
    @Test
    public void test() {
        ${_cname} bean = new ${_cname}();
        
        super.testInsertData(bean);
    } 
}
