package com.luckytom.patch.runnable;

import java.util.List;

import com.luckytom.patch.model.PatchProjectDTO;
import com.luckytom.patch.model.SettingDO;
import com.luckytom.patch.model.TeamPluginDO;
import com.luckytom.patch.util.AlertUtil;
import com.luckytom.patch.util.ConsoleUtil;
import com.luckytom.patch.util.FileUtil;
import com.luckytom.patch.util.MavenUtil;
import com.luckytom.patch.util.POMUtil;
import com.luckytom.patch.util.PatchUtil;
import com.luckytom.patch.util.SvnPatchUtil;

/**
 * 增量包runnable
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午11:22:07
 */
public class PatchRunnable implements Runnable {
	private SettingDO setting;

	public PatchRunnable(SettingDO setting) {
		this.setting = setting;
	}

	@Override
	public void run() {
		// 获取svn、git更新记录
		PatchProjectDTO patchProject = setting.getPatchProject();
		boolean isUpdate = PatchUtil.dealDependencyProjectList(patchProject.getDependencyProjectList(), setting.getSvnLogFilterParam());
		
		TeamPluginDO mainTeamPlugin = patchProject.getMainProject().getTeamPlugin();
		List<String> patchFileList = SvnPatchUtil.getPatchList(mainTeamPlugin, setting.getSvnLogFilterParam());
		if ((null == patchFileList || patchFileList.size() == 0) && !isUpdate) {
			AlertUtil.showInfoAlert("无更新文件，无补丁包可生成！");
		} else {
			// compile project
			String warPath = MavenUtil.getCompiledProject(patchProject);
			
			// unwar
			String tmpUnWarDirUrl = FileUtil.getTmpDirUrl();
			FileUtil.unWar(warPath, tmpUnWarDirUrl, tmpUnWarDirUrl);
			
			// generate patch
			String pomSvnUrl = POMUtil.getPOMPath(mainTeamPlugin.getServerUrl());
			TeamPluginDO pomTeamPlugin = new TeamPluginDO(pomSvnUrl, mainTeamPlugin.getUsername(), mainTeamPlugin.getPassword());
			PatchUtil.generatePatch(patchFileList, pomTeamPlugin, patchProject, setting.getPatchPath(), tmpUnWarDirUrl);
			
			String compileJarName = patchProject.getMainProject().getPackageDTO().getCompileJarName();
			ConsoleUtil.info(compileJarName + "补丁生成路径======>" + setting.getPatchPath());
			
			// delete temp file dir
			FileUtil.deleteTmpDir(tmpUnWarDirUrl);
		}
		SvnPatchUtil.disposeAllSvnOperationFactory();
	}

}
