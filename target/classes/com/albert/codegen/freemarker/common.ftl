<#assign _brack>{</#assign>
<#assign _uname>zhanggd</#assign>
<#assign _year>2016</#assign>
<#assign _company>chengdu 91csd technology Co., LTD</#assign>
<#assign _time>${obj.date?date}</#assign>
<#assign _pck>${obj.pak}</#assign>
<#assign _cpck>${_pck}.entity</#assign>
<#assign _cname>${obj.className}</#assign>
<#assign _cuname>${obj.classNameUpper}</#assign>
<#assign _clname>${_cname?uncap_first}</#assign>
<#assign _tablename>${obj.tableName}</#assign>
<#assign _description>${obj.argInfo.description}</#assign>
<#assign _haveFileField>${obj.haveFileField}</#assign>
<#assign _haveMixField>${obj.haveMixField}</#assign>
<#assign _attachFileFieldName>${obj.argInfo.attachFileFieldName}</#assign>
<#assign _mixFileFieldName>${obj.argInfo.mixFileFieldName}</#assign>
<#assign _cfullname>${_cpck}.${_cname}</#assign>
<#assign _servicepck>${_pck}.service</#assign>
<#assign _viewpck>${_pck}.view</#assign>
<#assign _controllpck>${_pck}.controller</#assign>
<#assign _viewqueryname>${_cname}QueryAction</#assign>
<#assign _viewcontrollname>${_cname}Controller</#assign>
<#assign _viewsavename>${_cname}SaveAction</#assign>
<#assign _servicename>${_cname}Service</#assign>
<#assign _serviceuname>${_clname}Service</#assign>
<#assign _servicefullname>${_servicepck}.${_servicename}</#assign>
<#assign _serviceimplpck>${_pck}.service.impl</#assign>
<#assign _serviceimplname>${_cname}ServiceImpl</#assign>
<#assign _serviceimpluname>${_clname}ServiceImpl</#assign>
<#assign _serviceimplfullname>${_serviceimplpck}.${_serviceimplname}</#assign>
<#assign _daopck>${_pck}.dao</#assign>
<#assign _daoname>${_cname}Dao</#assign>
<#assign _daouname>${_clname}Dao</#assign>
<#assign _daofullname>${_daopck}.${_daoname}</#assign>
<#assign _haveBizkeys>${obj.haveBizkeys}</#assign>