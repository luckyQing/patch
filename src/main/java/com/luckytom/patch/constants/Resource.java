package com.luckytom.patch.constants;

/**
 * 资源类
 *
 * @author luckytom
 * @version 1.0 2017年11月30日 下午11:32:54
 */
public final class Resource {

	public static final String APP_NAME = "Web项目（maven）增量包工具";
	
	public static final class Css {
		public static final String MAIN_UI = "css/main_ui.css";
	}

	public static final class Fxml {
		public static final String MAIN_UI = "fxml/main_ui.fxml";
	}

	public static final class Icon {
		public static final String APP = "icons/patch.jpg";
	}
	
	public static final String SVN_LOG_DETAIL = "版本号：%s ==>作者：%s ==>提交时间：%s ==>备注：%s";
	public static final String SNV_OPERATION_LOG = "====>%s：%s";
	
	public static final String TYPE_ADDED = "新增";
	public static final String TYPE_MODIFIED = "修改";
	public static final String TYPE_DELETED = "删除";
	public static final String TYPE_REPLACED = "替换";
	
	public static final String EXPORT_FILE_START = "export 开始==>%s";
	public static final String EXPORT_FILE_END = "export 结束==>%s";

	public static final String COMPILE_CHECK_POM = "======>pom.xml检查...";
	public static final String INSTALL_PORJECT = "======>install %s...";
	public static final String PACKAGE_PROJECT = "======>package %s...";
	public static final String COMPILE_RESULT = "%s路径======>%s";
	
	public static final String CHECK_ENV = "环境检查...";
	
	public static final String NO_PATCH = "无更新文件，无补丁包可生成！";
	
	public static final String PATCH_PATH = "%s补丁生成路径======>%s";
	
	public static final String MAVEN_NO_SETTING = "maven环境变量未设置！"; 
	public static final String JDK_NO_SETTING = "jdk环境变量未设置！"; 
	public static final String JDK_BELOW_VERSION8 = "请在jdk%s以上执行！"; 
}
