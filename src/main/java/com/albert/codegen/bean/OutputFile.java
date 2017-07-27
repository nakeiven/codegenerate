package com.albert.codegen.bean;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.albert.codegen.util.Constants;
import com.albert.codegen.util.TemlateEnum;

public class OutputFile {
	//private String projectPath = "";//工程根目录，下面包含src和conf两个子目录，src下又包含main和test两个子源码目录。
	
	private String srcMainPath = "";//主源码目录
	private String srcTestPath = "";//测试源码目录
	private String srcConfPath = "";//配置源码目录
	private String lastPack = "";//包的最后一段
	
	private String strutsXmlFile = null;//需要并入的struts配置文件名,不包含路径
	
	private String pck;
	private String className;
	private String outputType;
	private TemlateEnum temlateMenu;
	
	/*public String getProjectPath() {
		return projectPath;
	}*/
	
	private String normalPath(String path) {
        if (!path.endsWith(Constants.FILE_SEPARATOR))
            path = path + Constants.FILE_SEPARATOR;
        path = path.replaceAll("\\\\", "/");
        return path;
	}
	
	public void setProjectPath(String projectPath, String lastPack) {	    
	    this.lastPack = lastPack;
        if(StringUtils.isNotBlank(projectPath)){
            projectPath = normalPath(projectPath);
            //this.projectPath = projectPath;
            
            srcMainPath = projectPath + "main" + Constants.FILE_SEPARATOR + "java" + Constants.FILE_SEPARATOR;            
            srcConfPath = projectPath + "main" + Constants.FILE_SEPARATOR  + "resources" + Constants.FILE_SEPARATOR;                            
            srcTestPath = projectPath + "test" + Constants.FILE_SEPARATOR;//
            
            //智能判断，如果test目录下也有java目录，则定位到java子目录
            String lastPath = srcTestPath + "java" + Constants.FILE_SEPARATOR;
            File testFile = new File(lastPath);
            if (testFile.exists())
                srcTestPath = lastPath;
            
            srcMainPath = normalPath(srcMainPath);
            srcTestPath = normalPath(srcTestPath);
            srcConfPath = normalPath(srcConfPath);
        }
	}
	    
    public String getLastPack() {
        return lastPack;
    }
    
    public void setLastPack(String lastPack) {
        this.lastPack = lastPack;
    }

    public String getSrcMainPath() {
        return srcMainPath;
    }
    
    public void setSrcMainPath(String srcMainPath) {
        srcMainPath = normalPath(srcMainPath);
        this.srcMainPath = srcMainPath;
    }
    
    public String getSrcTestPath() {
        return srcTestPath;
    }
    
    public void setSrcTestPath(String srcTestPath) {
        srcTestPath = normalPath(srcTestPath);
        this.srcTestPath = srcTestPath;
    }
    
    public String getSrcConfPath() {
        return srcConfPath;
    }
    
    public void setSrcConfPath(String srcConfPath) {
        srcConfPath = normalPath(srcConfPath);
        this.srcConfPath = srcConfPath;
    }

    public String getPck() {
		return pck;
	}
	
	public void setPck(String pck) {
		this.pck = pck;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getOutputType() {
		return outputType;
	}
	
	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}
	
	public TemlateEnum getTemlateMenu() {
		return temlateMenu;
	}
	
	public void setTemlateMenu(TemlateEnum temlateMenu) {
		this.temlateMenu = temlateMenu;
	}
    
    public String getStrutsXmlFile() {
        return strutsXmlFile;
    }
    
    public void setStrutsXmlFile(String strutsXmlFile) {
        this.strutsXmlFile = strutsXmlFile;
    }
}
