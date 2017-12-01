package com.luckytom.patch.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luckytom.patch.constants.PatchDict;
import com.luckytom.patch.model.PackageDTO;

/**
 * 文件操作工具类
 * 
 * @author liyulin
 * @version 1.0 2017年11月29日 上午11:20:03
 */
public final class FileUtil {
	private static final Logger logger = LogManager.getFormatterLogger();

	public static boolean hasSeparator(String path) {
		return path.endsWith("/") || path.endsWith("\\");
	}
	
	private static void replaceSeparator(List<String> pathList, String separator) {
		if(null!=pathList&&pathList.size()>0) {
			String v = Matcher.quoteReplacement(File.separator);
			for(int i=0; i<pathList.size(); i++) {
				String path = pathList.get(i);
				path = path.replaceAll(separator, v);
				pathList.set(i, path);
			}
		}
	}

	public static void dealSeparator(List<String> pathList) {
		if(null!=pathList&&pathList.size()>0) {
			if(File.separator.equals("/")) {
				replaceSeparator(pathList, "\\");
			} else {
				replaceSeparator(pathList, "/");
			}
		}
	}
	
	/**
	 * 删除文件夹下面的所有文件
	 * 
	 * @param patchDir
	 */
	public static void deleteDirFiles(String patchDir) {
		File patchDirFile = new File(patchDir);
		if (patchDirFile.exists()) {
			File[] files = patchDirFile.listFiles();
			if (null != files && files.length > 0) {
				try {
					for (File file : files) {
						FileUtils.forceDelete(file);
					}
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 获取一个临时文件夹
	 * @return
	 */
	public static String getTmpDirUrl() {
		String tmpDirUrl = System.getProperty("java.io.tmpdir");
		if (tmpDirUrl.endsWith(File.separator)) {
			tmpDirUrl = tmpDirUrl + UUID.randomUUID().toString();
		} else {
			tmpDirUrl = tmpDirUrl + File.separator + UUID.randomUUID().toString();
		}
		
		String log = "tmpDirUrl=" + tmpDirUrl;
		logger.info(log);
		ConsoleUtil.info(log);
		
		return tmpDirUrl;
	}
	
	/**
	 * 获取路径中的项目名
	 * 
	 * @param path
	 * @return
	 */
	public static String getProjectName(String path) {
		int index1 = path.lastIndexOf("/");
		int index2 = path.lastIndexOf("\\");
		int index = Math.max(index1, index2) + 1;
		if (index == path.length()) {
			path = path.substring(0, path.length() - 1);
			return getProjectName(path);
		}
		String name = path.substring(index);
		return name;
	}

	/**
	 * 获取编译后的文件路径
	 * 
	 * @param projectPath
	 * @param compileJarName
	 * @return
	 */
	public static String getCompileJarPath(String projectPath, String compileJarName) {
		StringBuilder compiledProjectPath = new StringBuilder(100);
		compiledProjectPath.append(projectPath);
		if (!FileUtil.hasSeparator(projectPath)) {
			compiledProjectPath.append(File.separator);
		}
		compiledProjectPath.append("target").append(File.separator).append(compileJarName);

		return compiledProjectPath.toString();
	}
	
	/**
	 * 获取WEB-INFO下面对应的class、resource文件路径
	 * @param patchDir
	 * @param packagingName
	 * @param packagingFilePath
	 * @return
	 */
	public static String getSrcClassesPath(String patchDir, String packagingName, String packagingFilePath){
		StringBuilder path = new StringBuilder(patchDir);
		path.append(File.separator);
		if(StringUtils.isNotBlank(packagingName)) {
			path.append(packagingName)
				.append(File.separator);
		}
		path.append(PatchDict.ProjectInfo.WEB_INFO)
			.append(File.separator)
			.append(PatchDict.ProjectInfo.CLASSES)
			.append(File.separator)
		    .append(packagingFilePath);
		
		return path.toString();
	}

	public static void copyFile(String srcPath, String destPath) {
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		try {
			FileUtils.copyFile(srcFile, destFile);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			ConsoleUtil.error(e.getMessage());
		}
	}
	
	/**
	 * 复制依赖包
	 * @param compileMainProjectPath
	 * @param destFileDir
	 * @param mainProjectName
	 * @return
	 */
	public static boolean copyDependencyProject(String compileMainProjectPath, String destFileDir, PackageDTO dependencyPackage) {
		String mainProjectName = FileUtil.getProjectName(compileMainProjectPath);
		StringBuilder srcFilePath = new StringBuilder(compileMainProjectPath);
		if (!hasSeparator(destFileDir)) {
			srcFilePath.append(File.separator);
		}
		srcFilePath.append(File.separator)
				   .append(PatchDict.ProjectInfo.WEB_INFO)
				   .append(File.separator)
				   .append(PatchDict.ProjectInfo.LIB).append(File.separator).append(dependencyPackage.getCompileJarName());
		
		StringBuilder destFilePath = new StringBuilder(destFileDir);
		if (!hasSeparator(destFileDir)) {
			destFilePath.append(File.separator);
		}
		destFilePath.append(mainProjectName)
					.append(File.separator)
					.append(PatchDict.ProjectInfo.WEB_INFO)
					.append(File.separator)
					.append(PatchDict.ProjectInfo.LIB).append(File.separator).append(dependencyPackage.getCompileJarName());
		
		copyFile(srcFilePath.toString(), destFilePath.toString());
		return true;
	}
	
	/**
	 * 解压war
	 * 
	 * @param warPath
	 * @param patchPath
	 * @param mainProjectName
	 */
	public static boolean unWar(String warPath, String unWarDir, String mainProjectName) {
		StringBuilder unWarPath = new StringBuilder(unWarDir);
		if(!FileUtil.hasSeparator(unWarDir)) {
			unWarPath.append(File.separator);
		}
		unWarPath.append(mainProjectName);
		return WarUtil.unWar(warPath, unWarPath.toString());
	}
	
	/**
	 * 删除临时文件夹
	 * 
	 * @param tmpDirUrl
	 * @return
	 */
	public static boolean deleteTmpDir(String tmpDirUrl) {
		boolean opState = true;
		try {
			FileUtils.deleteDirectory(new File(tmpDirUrl));
		} catch (IOException e) {
			opState = false;
			logger.error(e.getMessage(), e);
			ConsoleUtil.error(e.getMessage());
		}
		return opState;
	}
	
	public static String getCompileJarName(String name, String version, String packagingType) {
		return name + "-" + version + "." + packagingType;
	}
	
	public static String getCompileWarName(String name, String packagingType) {
		return name + "." + packagingType;
	}
	
	public static String appendChildDir(String path, String childDir) {
		if (hasSeparator(path)) {
			return path + childDir;
		}
		return path + File.separator + childDir;
	}
}
