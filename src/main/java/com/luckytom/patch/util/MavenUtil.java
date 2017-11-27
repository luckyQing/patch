package com.luckytom.patch.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.luckytom.patch.model.ProjectDTO;
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
	public static final String MAVEN_THREAD_PARAMETER = "-T"+CPU_CORE_NUM;
	
	public static class EnvCheck {
		private static final String MAVEN_HOME_KEY = "MAVEN_HOME";
		private static final String JAVA_HOME_KEY = "JAVA_HOME";
		private static final Map<String, String> ENV_MAP = System.getenv();

		public static boolean checkMavenEnv() {
			boolean pass = StringUtils.isNotBlank(ENV_MAP.get(MAVEN_HOME_KEY));
			if (!pass) {
				ConsoleUtil.info("maven环境变量未设置！");
			}
			return pass;
		}

		public static boolean checkJDKEnv() {
			boolean pass = StringUtils.isNotBlank(ENV_MAP.get(JAVA_HOME_KEY));
			if (!pass) {
				ConsoleUtil.info("jdk环境变量未设置！");
			}
			return pass;
		}

	}

	public static boolean mvnCleanPackage(String projectPath) {
		List<String> commandList = new ArrayList<String>();
		dealCmd(projectPath, commandList);

		commandList.add("cd " + projectPath);
		commandList.add("mvn clean package " + MAVEN_THREAD_PARAMETER + " -Dmaven.test.skip=true");
		return CmdUtil.exeCmds(commandList.toArray(new String[commandList.size()]));
	}

	public static boolean mvnInstall(String projectPath) {
		List<String> commandList = new ArrayList<String>();
		dealCmd(projectPath, commandList);
		commandList.add("cd " + projectPath);
		commandList.add("mvn install " + MAVEN_THREAD_PARAMETER + " -Dmaven.test.skip=true");
		return CmdUtil.exeCmds(commandList.toArray(new String[commandList.size()]));
	}

	private static void dealCmd(String projectPath, List<String> commandList) {
		String key = ":";
		if (projectPath.contains(key)) {
			String cmd = projectPath.substring(0, projectPath.indexOf(key) + 1);
			commandList.add(cmd);
		}
	}

	@SuppressWarnings("deprecation")
	public static String getCompiledProject(String projectPath, List<String> dependencyProjectList) {
		ConsoleUtil.info("======>pom.xml检查...");
		MavenWebEnvService.initMavenWebEnv(projectPath);
		if (null != dependencyProjectList) {
			for (String dependencyProject : dependencyProjectList) {
				ProjectDTO dependencyProjectDTO = MavenProjectUtil.getProjectInfo(dependencyProject);
				ConsoleUtil.info("======>install " + dependencyProjectDTO.getName() + "...");
				
				if (!MavenUtil.mvnInstall(dependencyProject)) {
					Thread.currentThread().stop();
				}
			}
		}

		ProjectDTO projectDTO = MavenProjectUtil.getProjectInfo(projectPath);
		ConsoleUtil.info("======>package " + projectDTO.getName() + "...");
		if (!MavenUtil.mvnCleanPackage(projectPath)) {
			Thread.currentThread().stop();
		}
		
		StringBuilder compiledProjectPath = new StringBuilder(100);
		compiledProjectPath.append(projectPath)
						   .append(File.separator)
						   .append("target")
						   .append(File.separator)
						   .append(projectDTO.getName())
						   .append(".")
						   .append(projectDTO.getPackagingType());
		
		ConsoleUtil.info(projectDTO.getName()+"."+projectDTO.getPackagingType()+"路径======>"+compiledProjectPath.toString());
		
		return compiledProjectPath.toString();
	}
	
}
