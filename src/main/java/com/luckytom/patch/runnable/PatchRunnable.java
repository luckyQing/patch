package com.luckytom.patch.runnable;

import java.util.List;

import com.luckytom.patch.util.MavenUtil;

/**
 * 增量包runnable
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午11:22:07
 */
public class PatchRunnable implements Runnable {
	private String projectPath;
	private List<String> dependencyProjectList;

	public PatchRunnable(String projectPath, List<String> dependencyProjectList) {
		this.projectPath = projectPath;
		this.dependencyProjectList = dependencyProjectList;
	}

	@Override
	public void run() {
		// 获取svn、git更新记录

		// 编译项目
		String _package = MavenUtil.getCompiledProject(projectPath, dependencyProjectList);
	}

}
