<#include "common.ftl">
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE struts PUBLIC
    "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
    "http://struts.apache.org/dtds/struts-2.0.dtd">
<struts>	
	<package name="${_pck}" namespace="/${_clname}" extends="xml-default">
		<action name="query${_cname}" class="${_viewpck}.${_viewqueryname}">
		    <result name="success"></result>
		</action>
		<action name="save${_cname}" class="${_viewpck}.${_viewsavename}">
		    <result name="success"></result>
		</action>
	</package>
</struts>
