package com.luckytom.patch.model;

import java.io.Serializable;
import java.util.List;

/**
 * 配置信息
 *
 * @author luckytom
 * @version 1.0 2017年11月5日 上午11:55:15
 */
public class SettingDO implements Serializable {

	private static final long serialVersionUID = -8514517532101610157L;
	/** 控制台日志开关 */
	private boolean logSwitch;
	/** 补丁生成路径 */
	private String patchPath;
	/** 补丁相关工程 */
	private PatchProjectDTO patchProject;
	/** svn日志过滤参数 */
	private LogFilterParam logFilterParam;
	private List<String> notDealFileList;

	public SettingDO() {
		super();
	}

	public SettingDO(boolean logSwitch, String patchPath, PatchProjectDTO patchProject, LogFilterParam logFilterParam,
			List<String> notDealFileList) {
		super();
		this.logSwitch = logSwitch;
		this.patchPath = patchPath;
		this.patchProject = patchProject;
		this.logFilterParam = logFilterParam;
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

	public PatchProjectDTO getPatchProject() {
		return patchProject;
	}

	public void setPatchProject(PatchProjectDTO patchProject) {
		this.patchProject = patchProject;
	}

	public LogFilterParam getLogFilterParam() {
		return logFilterParam;
	}

	public void setLogFilterParam(LogFilterParam logFilterParam) {
		this.logFilterParam = logFilterParam;
	}

	public List<String> getNotDealFileList() {
		return notDealFileList;
	}

	public void setNotDealFileList(List<String> notDealFileList) {
		this.notDealFileList = notDealFileList;
	}

	@Override
	public String toString() {
		return "SettingDO [logSwitch=" + logSwitch + ", patchPath=" + patchPath + ", patchProject=" + patchProject
				+ ", logFilterParam=" + logFilterParam + ", notDealFileList=" + notDealFileList + "]";
	}

}
