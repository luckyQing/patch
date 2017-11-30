package com.luckytom.patch.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import com.luckytom.patch.constants.PatchDict;
import com.luckytom.patch.model.PatchProjectDTO;
import com.luckytom.patch.model.PatchProjectInfoDTO;
import com.luckytom.patch.model.SVNLogFilterParam;
import com.luckytom.patch.model.TeamPluginDO;

/**
 * 应用工具类
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午9:33:35
 */
public final class PatchUtil {
	private static final Logger logger = LogManager.getFormatterLogger();
	
	/**
	 * deal dependency projects
	 * 
	 * @param dependencyProjectList
	 * @param svnLogFilterParam
	 * @return
	 */
	public static boolean dealDependencyProjectList(List<PatchProjectInfoDTO> dependencyProjectList, SVNLogFilterParam svnLogFilterParam) {
		boolean isUpdate = false;
		for (PatchProjectInfoDTO dependencyProject : dependencyProjectList) {
			List<String> dependencyProjectFileList = SvnPatchUtil.getPatchList(dependencyProject.getTeamPlugin(), svnLogFilterParam);
			if (null != dependencyProjectFileList && dependencyProjectFileList.size() > 0) {
				isUpdate = true;
				dependencyProject.setUpdate(isUpdate);
			}
		}
		return isUpdate;
	}

	public static boolean generatePatch(List<String> updateFileList, TeamPluginDO pomTeamPlugin, PatchProjectDTO patchProject, String patchDir, String tmpUnWarDirUrl) {
		if (null == updateFileList || updateFileList.size() == 0) {
			logger.info("无变更记录，无补丁包生成！");
			return true;
		}

		List<PatchProjectInfoDTO> dependencyProjectList = patchProject.getDependencyProjectList();
		PatchProjectInfoDTO mainProject = patchProject.getMainProject();
		String packagingName = mainProject.getPackageDTO().getPackagingName();
		String compileMainProjectPath = tmpUnWarDirUrl +File.separator + packagingName;
		// 1、main project
		// 1.1 main/java
		dealJavaFiles(packagingName, compileMainProjectPath, patchDir, updateFileList);
		// 1.2 main/recource
		dealResource(packagingName, compileMainProjectPath, patchDir, updateFileList);
		// 1.3 webapp
		dealWebapp(packagingName, compileMainProjectPath, patchDir, updateFileList);
		// 1.4 pom.xml
		dealPOM(updateFileList, pomTeamPlugin, compileMainProjectPath, patchDir);
		
		// 2、dependency project
		for (PatchProjectInfoDTO dependencyProject : dependencyProjectList) {
			FileUtil.copyDependencyProject(dependencyProject, patchDir, packagingName);
		}
		return false;
	}
	
	public static void dealWebapp() {
		
	}

	/**
	 * 判断pom.xml是否更新
	 * @param packagingName
	 * @param updateFileList
	 * @return
	 */
	private static boolean isUpdatePOM(String packagingName, List<String> updateFileList) {
		boolean isUpdate = false;
		String pomPath = packagingName + File.separator + "pom.xml";
		Iterator<String> updateFileIterable = updateFileList.iterator();
		while (updateFileIterable.hasNext()) {
			if (updateFileIterable.next().contains(pomPath)) {
				isUpdate = true;
				break;
			}
		}
		
		return isUpdate;
	}
	
	/**
	 * 处理pom.xml
	 * 
	 * @param updateFileList
	 * @param pomTeamPlugin
	 * @param mainProjectPath
	 * @param patchDir
	 */
	private static void dealPOM(List<String> updateFileList, TeamPluginDO pomTeamPlugin, String mainProjectPath, String patchDir) {
		String packagingName = FileUtil.getProjectName(mainProjectPath);
		boolean isUpdate = isUpdatePOM(packagingName, updateFileList);
		if (isUpdate) {
			FileUtil.copyPOM(mainProjectPath, packagingName, patchDir);
			dealPOMJar(pomTeamPlugin, mainProjectPath, patchDir);
		}
	}

	/**
	 * 处理pom.xml新增的jar
	 * 
	 * @param teamPlugin svn上pom.xml的相关信息
	 * @param projectPath 编译后的工程路径
	 * @param patchDir 补丁包路径
	 */
	private static void dealPOMJar(TeamPluginDO teamPlugin, String projectPath, String patchDir) {
		String saveUrl = projectPath + File.separator + "pom_old.xml";
		boolean isUpdate = SvnPatchUtil.exportOldPOM(teamPlugin, saveUrl);
		if(isUpdate) {
			Model newModel = POMUtil.readModel(projectPath);
			List<Dependency> newDependencyList = newModel.getDependencies();
			
			Model oldModel = POMUtil.readModel(saveUrl);
			List<Dependency> oldDependencyList = oldModel.getDependencies();
			
			for(Dependency newDependency:newDependencyList) {
				boolean isNew = true;
				for(Dependency oldDependency:oldDependencyList) {
					if(newDependency.getGroupId().equals(oldDependency.getGroupId()) && 
							newDependency.getArtifactId().equals(oldDependency.getArtifactId()) && 
							newDependency.getVersion().equals(oldDependency.getVersion())) {
						isNew = false;
						break;
					}
				}
				
				if(isNew) {
					String jarName = FileUtil.getCompileJarName(newDependency.getArtifactId(), newDependency.getVersion(), newDependency.getType());
					
					StringBuilder jarPath = new StringBuilder(PatchDict.StringCapacity.FILE_PATH);
					jarPath.append(projectPath)
						   .append(File.separator)
						   .append(PatchDict.ProjectInfo.WEB_INFO)
						   .append(File.separator)
						   .append(PatchDict.ProjectInfo.LIB)
						   .append(File.separator)
						   .append(jarName);
					
					StringBuilder destPath = new StringBuilder(PatchDict.StringCapacity.FILE_PATH);
					destPath.append(patchDir).append(File.separator)
						    .append(newModel.getArtifactId())
						    .append(File.separator)
						    .append(PatchDict.ProjectInfo.WEB_INFO)
						    .append(File.separator)
						    .append(PatchDict.ProjectInfo.LIB)
						    .append(File.separator)
						    .append(jarName);
					
					FileUtil.copyFile(jarPath.toString(), destPath.toString());
				}
			}
			
		}
	}
	
	/**
	 * 处理webapp下所有更新文件
	 * 
	 * @param packagingName
	 * @param projectPath
	 * @param patchDir
	 * @param updateFileList
	 */
	private static void dealWebapp(String packagingName, String projectPath, String patchDir, List<String> updateFileList) {
		String key = packagingName+File.separator+PatchDict.ProjectInfo.SRC_MAIN_WEBAPP;
		Iterator<String> updateFileIterable = updateFileList.iterator();
		while (updateFileIterable.hasNext()) {
			String svnUrl = updateFileIterable.next();
			if (svnUrl.contains(key)) {
				String name = svnUrl.substring(svnUrl.indexOf(key)+key.length(), svnUrl.length());
				StringBuilder srcPath = new StringBuilder(PatchDict.StringCapacity.FILE_PATH);
				srcPath.append(projectPath)
					   .append(File.separator)
					   .append(name);
				
				StringBuilder destPath = new StringBuilder(PatchDict.StringCapacity.FILE_PATH);
				destPath.append(patchDir)
						.append(File.separator)
						.append(packagingName)
						.append(File.separator)
					    .append(name);
				
				FileUtil.copyFile(srcPath.toString(), destPath.toString());
				break;
			}
		}
	}
	
	/**
	 * 处理resource下所有更新文件
	 * 
	 * @param packagingName
	 * @param projectPath
	 * @param patchDir
	 * @param updateFileList
	 */
	private static void dealResource(String packagingName, String projectPath, String patchDir, List<String> updateFileList) {
		String key = packagingName+File.separator+PatchDict.ProjectInfo.SRC_MAIN_RESOURCE;
		Iterator<String> updateFileIterable = updateFileList.iterator();
		while (updateFileIterable.hasNext()) {
			String svnUrl = updateFileIterable.next();
			if (svnUrl.contains(key)) {
				String packagingFilePath = svnUrl.substring(svnUrl.indexOf(key)+key.length(), svnUrl.length());
				StringBuilder srcPath = new StringBuilder(PatchDict.StringCapacity.FILE_PATH);
				srcPath.append(projectPath)
					   .append(File.separator)
					   .append(packagingFilePath);
				String destPath = FileUtil.getSrcClassesPath(patchDir, packagingName, packagingFilePath);
				FileUtil.copyFile(srcPath.toString(), destPath);
				break;
			}
		}
	}
	
	/**
	 * deal updated java files
	 * 
	 * @param packagingName
	 * @param projectPath
	 * @param patchDir
	 * @param updateFileList
	 */
	private static void dealJavaFiles(String packagingName, String projectPath, String patchDir, List<String> updateFileList) {
		String key = packagingName+File.separator+PatchDict.ProjectInfo.SRC_MAIN_JAVA;
		Iterator<String> updateFileIterable = updateFileList.iterator();
		while (updateFileIterable.hasNext()) {
			String svnUrl = updateFileIterable.next();
			if (svnUrl.contains(key)) {
				String packagingFilePath = svnUrl.substring(svnUrl.indexOf(key)+key.length(), svnUrl.length());
				String srcPath = StringUtils.EMPTY;
				if(packagingFilePath.endsWith(PatchDict.ProjectInfo.DOT_JAVA)) {
					packagingFilePath = packagingFilePath.replace(PatchDict.ProjectInfo.DOT_JAVA, PatchDict.ProjectInfo.DOT_CLASS);
					srcPath = projectPath + File.separator +packagingFilePath;
					dealInnerClass(srcPath, packagingName, patchDir);
				} else {
					srcPath = projectPath + File.separator +packagingFilePath;
				}
				
				String destPath = FileUtil.getSrcClassesPath(patchDir, packagingName, packagingFilePath);
				FileUtil.copyFile(srcPath, destPath);
				break;
			}
		}
	}
	
	public static void dealInnerClass(String srcPath, String packagingName, String patchDir) {
		File srcFile = new File(srcPath);
		String srcFileName = srcFile.getName().replace(PatchDict.ProjectInfo.DOT_CLASS, "");
		String innerClassRegex = srcFileName + "\\$[_A-Za-z][_A-Za-z0-9]{0,}"+PatchDict.ProjectInfo.DOT_CLASS;
		
		File[] srcDirFileList = new File(srcFile.getParent()).listFiles();
		for(File srcDirFile:srcDirFileList){
			if(srcDirFile.getName().matches(innerClassRegex)){
				String innerClassPath = srcDirFile.getPath();
				String key = PatchDict.ProjectInfo.WEB_INFO_CLASSES;
				String packagingFilePath = innerClassPath.substring(innerClassPath.indexOf(key)+key.length());
				String destPath = FileUtil.getSrcClassesPath(patchDir, packagingName, packagingFilePath);
				FileUtil.copyFile(innerClassPath, destPath);
			}
		}
	}
}
