package com.luckytom.patch.service;

import java.util.List;

import com.luckytom.patch.runnable.PatchRunnable;
import com.luckytom.patch.util.MavenUtil;
import com.luckytom.patch.util.ConsoleUtil;

/**
 * 应用service
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午9:42:03
 */
public class PatchService {
	
	/**
	 * 编译项目
	 * 
	 * @param projectPath
	 * @param dependencyProjectList
	 */
	public static void generatePatch(String projectPath, List<String> dependencyProjectList) {
		ConsoleUtil.info("环境检查...");
		if (MavenUtil.EnvCheck.checkJDKEnv() && MavenUtil.EnvCheck.checkMavenEnv()) {
			Thread thread = new Thread(new PatchRunnable(projectPath, dependencyProjectList));
			thread.setDaemon(true);
			thread.start();
		}
	}
	
}
