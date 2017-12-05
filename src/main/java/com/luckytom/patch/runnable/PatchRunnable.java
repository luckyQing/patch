package com.luckytom.patch.runnable;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luckytom.patch.constants.Resource;
import com.luckytom.patch.model.PatchProjectDTO;
import com.luckytom.patch.model.SettingDO;
import com.luckytom.patch.model.TeamPluginDO;
import com.luckytom.patch.teamPlugin.util.SvnPatchUtil;
import com.luckytom.patch.util.AlertUtil;
import com.luckytom.patch.util.FileUtil;
import com.luckytom.patch.util.MavenUtil;
import com.luckytom.patch.util.POMUtil;
import com.luckytom.patch.util.PatchUtil;

/**
 * 增量包runnable
 * 
 * @author luckytom
 * @version 1.0 2017年11月23日 上午11:22:07
 */
public class PatchRunnable implements Runnable {
	
	private static final Logger logger = LogManager.getFormatterLogger();
	private SettingDO setting;

	public PatchRunnable(SettingDO setting) {
		this.setting = setting;
	}

	@Override
	public void run() {
		// 获取svn、git更新记录
		PatchProjectDTO patchProject = setting.getPatchProject();
		boolean isUpdate = PatchUtil.dealDependencyProjectList(patchProject.getDependencyProjectList(), setting.getLogFilterParam());
		
		TeamPluginDO mainTeamPlugin = patchProject.getMainProject().getTeamPlugin();
		List<String> patchFileList = SvnPatchUtil.getPatchList(mainTeamPlugin, setting.getLogFilterParam());
		if ((null == patchFileList || patchFileList.size() == 0) && !isUpdate) {
			AlertUtil.showInfoAlert(Resource.NO_PATCH);
		} else {
			FileUtil.dealSeparator(patchFileList);
			
			// compile project
			String warPath = MavenUtil.compileProject(patchProject);
			
			// unwar
			String tmpUnWarDirUrl = FileUtil.generateTempDirUrl();
			FileUtil.unWar(warPath, tmpUnWarDirUrl, patchProject.getMainProject().getPackageDTO().getPackagingName());
			
			// generate patch
			String pomSvnUrl = POMUtil.getPOMPath(mainTeamPlugin.getServerUrl());
			TeamPluginDO pomTeamPlugin = new TeamPluginDO(pomSvnUrl, mainTeamPlugin.getUsername(), mainTeamPlugin.getPassword());
			PatchUtil.generatePatch(patchFileList, pomTeamPlugin, patchProject, setting.getPatchPath(), tmpUnWarDirUrl);
			
			String compileJarName = patchProject.getMainProject().getPackageDTO().getCompileJarName();
			String patchPath = String.format(Resource.PATCH_PATH, compileJarName, setting.getPatchPath());
			logger.info(patchPath);
			
			// delete temp file dir
			FileUtil.deleteDir(tmpUnWarDirUrl);
		}
	}

}
