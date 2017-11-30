package com.luckytom.patch.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.luckytom.patch.constants.Strings;
import com.luckytom.patch.model.PackageDTO;
import com.luckytom.patch.model.PatchProjectDTO;
import com.luckytom.patch.model.PatchProjectInfoDTO;
import com.luckytom.patch.service.MavenWebEnvService;

/**
 * maven工具类
 *
 * @author liyulin
 * @version 1.0 2017年11月21日 上午10:27:06
 */
public final class MavenUtil {

	/** cpu内核数 */
	public static final int CPU_CORE_NUM = Runtime.getRuntime().availableProcessors();
	public static final String MAVEN_THREAD_PARAMETER = "-T" + CPU_CORE_NUM;

	public static class EnvCheck {
		private static final String MAVEN_HOME_KEY = "MAVEN_HOME";
		private static final String JAVA_HOME_KEY = "JAVA_HOME";
		private static final Map<String, String> ENV_MAP = System.getenv();

		public static boolean checkMavenEnv() {
			boolean pass = StringUtils.isNotBlank(ENV_MAP.get(MAVEN_HOME_KEY));
			if (!pass) {
				ConsoleUtil.error("maven环境变量未设置！");
			}
			return pass;
		}

		public static boolean checkJDKEnv() {
			boolean pass = StringUtils.isNotBlank(ENV_MAP.get(JAVA_HOME_KEY));
			if (!pass) {
				ConsoleUtil.error("jdk环境变量未设置！");
			}
			return pass;
		}

	}

	public static void mvnCleanPackage(String projectPath) {
		List<String> commandList = new ArrayList<String>();
		dealCmd(projectPath, commandList);

		commandList.add("cd " + projectPath);
		commandList.add("mvn clean package " + MAVEN_THREAD_PARAMETER + " -Dmaven.test.skip=true");
		CmdUtil.exeCmds(commandList.toArray(new String[commandList.size()]));
	}

	public static void mvnInstall(String projectPath) {
		List<String> commandList = new ArrayList<String>();
		dealCmd(projectPath, commandList);
		commandList.add("cd " + projectPath);
		commandList.add("mvn install " + MAVEN_THREAD_PARAMETER + " -Dmaven.test.skip=true");
		CmdUtil.exeCmds(commandList.toArray(new String[commandList.size()]));
	}

	private static void dealCmd(String projectPath, List<String> commandList) {
		String key = ":";
		if (projectPath.contains(key)) {
			String cmd = projectPath.substring(0, projectPath.indexOf(key) + 1);
			commandList.add(cmd);
		}
	}

	public static String getCompiledProject(PatchProjectDTO patchProject) {
		ConsoleUtil.info(Strings.COMPILE_CHECK_POM);
		PatchProjectInfoDTO mainProject = patchProject.getMainProject();
		List<PatchProjectInfoDTO> dependencyProjectList = patchProject.getDependencyProjectList();
		
		MavenWebEnvService.initMavenWebEnv(mainProject.getPath());
		if (null != dependencyProjectList) {
			for (PatchProjectInfoDTO dependencyProject : dependencyProjectList) {
				PackageDTO dependencyPackageDTO = POMUtil.getPackageDTO(dependencyProject.getPath());
				dependencyProject.setPackageDTO(dependencyPackageDTO);
				ConsoleUtil.info(String.format(Strings.INSTALL_PORJECT, dependencyPackageDTO.getPackagingName()));
				
				MavenUtil.mvnInstall(dependencyProject.getPath());
			}
		}

		PackageDTO mainPackageDTO = POMUtil.getPackageDTO(mainProject.getPath());
		mainProject.setPackageDTO(mainPackageDTO);
		ConsoleUtil.info(String.format(Strings.PACKAGE_PROJECT, mainPackageDTO.getPackagingName()));
		MavenUtil.mvnCleanPackage(mainProject.getPath());

		String compiledProjectPath = FileUtil.getCompileJarPath(mainProject.getPath(), mainPackageDTO.getCompileJarName());
		String result = String.format(Strings.COMPILE_RESULT, mainPackageDTO.getCompileJarName(), compiledProjectPath);
		ConsoleUtil.info(result);
		
		return compiledProjectPath.toString();
	}

}
