package com.luckytom.patch.constants;

import java.io.File;

/**
 * 常量类
 *
 * @author liyulin
 * @version 1.0 2017年11月5日 上午11:54:36
 */
public final class PatchDict {
	public static final String APP_NAME = "Web项目（maven）增量包工具";
	
	public static final class StringCapacity{
		public static final int FILE_PATH = 100;
	}

	public static final class Css {
		public static final String MAIN_UI = "css/main_ui.css";
	}

	public static final class Fxml {
		public static final String MAIN_UI = "fxml/main_ui.fxml";
	}

	public static final class Icon {
		public static final String APP = "icons/patch.jpg";
	}

	public static final class ArchiveType{
		public static final String JAR = "jar";
		public static final String WAR = "war";
	}
	
	public static final class ProjectInfo {
		public static final String LIB = "lib";
		public static final String JAVA = "java";
		public static final String DOT_JAVA = ".java";
		public static final String DOT_CLASS = ".class";
		public static final String CLASSES = "classes";
		public static final String WEB_INFO = "WEB-INF";
		public static final String WEB_APP = "webapp";
		public static final String RESOURCE = "resource";
		public static final String SRC_MAIN_WEBAPP = "src" + File.separator + "main" + File.separator + WEB_APP + File.separator;
		public static final String WEB_INFO_CLASSES = PatchDict.ProjectInfo.WEB_INFO + File.separator + PatchDict.ProjectInfo.CLASSES + File.separator;
		public static final String SRC_MAIN_RESOURCE = "src" + File.separator + "main" + File.separator  + RESOURCE + File.separator;
		public static final String SRC_MAIN_JAVA = "src" + File.separator + "main" + File.separator + JAVA + File.separator;
	}

}
