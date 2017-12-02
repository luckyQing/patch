package com.luckytom.patch.constants;

import java.io.File;

/**
 * 常量类
 *
 * @author luckytom
 * @version 1.0 2017年11月5日 上午11:54:36
 */
public final class Constants {
	
	/**运行时要求的jdk版本*/
	public static final String REQUIRED_JDK_VERSION = "1.8";
	
	public static final class StringCapacity{
		public static final int FILE_PATH = 100;
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
		public static final String WEB_INFO_CLASSES = Constants.ProjectInfo.WEB_INFO + File.separator + Constants.ProjectInfo.CLASSES + File.separator;
		public static final String SRC_MAIN_RESOURCE = "src" + File.separator + "main" + File.separator  + RESOURCE + File.separator;
		public static final String SRC_MAIN_JAVA = "src" + File.separator + "main" + File.separator + JAVA + File.separator;
	}

}
