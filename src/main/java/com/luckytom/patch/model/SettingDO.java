package com.luckytom.patch.model;

import java.util.List;

/**
 * 配置信息
 *
 * @author liyulin
 * @version 1.0 2017年11月5日 上午11:55:15
 */
public class SettingDO {
	private boolean logSwitch;
	private String patchPath;
	private String localProjectPath;
	private TeamPluginDO teamPluginDO;
	private List<String> notDealFileList;

	public SettingDO() {
		super();
	}

	public SettingDO(boolean logSwitch, String patchPath, String localProjectPath, TeamPluginDO teamPluginDO,
			List<String> notDealFileList) {
		super();
		this.logSwitch = logSwitch;
		this.patchPath = patchPath;
		this.localProjectPath = localProjectPath;
		this.teamPluginDO = teamPluginDO;
		this.notDealFileList = notDealFileList;
	}

	public boolean isLogSwitch() {
		return logSwitch;
	}

	public void setLogSwitch(boolean logSwitch) {
		this.logSwitch = logSwitch;
	}

	public String getPatchPath() {
		return patchPath;
	}

	public void setPatchPath(String patchPath) {
		this.patchPath = patchPath;
	}

	public String getLocalProjectPath() {
		return localProjectPath;
	}

	public void setLocalProjectPath(String localProjectPath) {
		this.localProjectPath = localProjectPath;
	}

	public TeamPluginDO getTeamPluginDO() {
		return teamPluginDO;
	}

	public void setTeamPluginDO(TeamPluginDO teamPluginDO) {
		this.teamPluginDO = teamPluginDO;
	}

	public List<String> getNotDealFileList() {
		return notDealFileList;
	}

	public void setNotDealFileList(List<String> notDealFileList) {
		this.notDealFileList = notDealFileList;
	}

	@Override
	public String toString() {
		return "SettingDO [logSwitch=" + logSwitch + ", patchPath=" + patchPath + ", localProjectPath="
				+ localProjectPath + ", teamPluginDO=" + teamPluginDO + ", notDealFileList=" + notDealFileList + "]";
	}

}
