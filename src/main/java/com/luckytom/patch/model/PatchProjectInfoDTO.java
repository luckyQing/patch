package com.luckytom.patch.model;

import com.luckytom.patch.util.FileUtil;

/**
 * 补丁工程信息
 * 
 * @author luckytom
 * @version 1.0 2017年11月29日 上午11:06:35
 */
public class PatchProjectInfoDTO {
	/** 工程路径 */
	private String path;
	/** 是否有修改 */
	private transient boolean update;
	/** 工程名 */
	private transient volatile String name;
	private transient PackageDTO packageDTO;
	private TeamPluginDO teamPlugin;

	public PatchProjectInfoDTO() {
		super();
	}

	public PatchProjectInfoDTO(String path, TeamPluginDO teamPlugin) {
		super();
		this.path = path;
		this.teamPlugin = teamPlugin;
		this.update = false;
	}

	public PatchProjectInfoDTO(String path, boolean update) {
		super();
		this.path = path;
		this.update = update;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public String getProjectName() {
		if (name == null) {
			name = FileUtil.getProjectName(path);
		}
		return name;
	}

	public PackageDTO getPackageDTO() {
		return packageDTO;
	}

	public void setPackageDTO(PackageDTO packageDTO) {
		this.packageDTO = packageDTO;
	}

	public TeamPluginDO getTeamPlugin() {
		return teamPlugin;
	}

	public void setTeamPlugin(TeamPluginDO teamPlugin) {
		this.teamPlugin = teamPlugin;
	}

	@Override
	public String toString() {
		return "PatchProjectInfoDTO [path=" + path + ", update=" + update + ", name=" + name + ", packageDTO="
				+ packageDTO + ", teamPlugin=" + teamPlugin + "]";
	}

}
