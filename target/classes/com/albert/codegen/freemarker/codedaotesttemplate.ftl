<#include "common.ftl">
package ${_pck};

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import com.manfen.test.BaseSpringTestCase;
import ${_cfullname};
import ${_servicefullname};

public class ${_cname}ServiceTest extends BaseSpringTestCase {
	@Autowired
    private ${_servicename} ${_serviceuname};
    
    private static ${obj.pkJavaType} pkObj;
			
	@Test
	@Rollback(false)
	public void testInsert${_cname}() {
		${_cname} ${_clname} = new ${_cname}();
		${_serviceuname}.create(${_clname});
		pkObj = ${_clname}.get${obj.primaryKey?cap_first}();
	}
	
	@Test
	@Rollback(false)
	public void testUpdate${_cname}() {		
		${_cname} ${_clname} = ${_serviceuname}.get(pkObj);
        ${_serviceuname}.update(${_clname});
	}
	
	@Test
	@Rollback(false)
	public void testDelete${_cname}ById() {
		${_serviceuname}.delete(pkObj);
		//判断为null
		${_cname} ${_clname} = ${_serviceuname}.get(pkObj);
		Assert.assertNull(${_clname});
	}
}
