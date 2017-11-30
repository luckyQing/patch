package com.luckytom.patch.model;

import java.util.List;

/**
 * 补丁相关工程
 * 
 * @author liyulin
 * @version 1.0 2017年11月29日 上午10:49:07
 */
public class PatchProjectDTO {
	/** 主工程 */
	private PatchProjectInfoDTO mainProject;
	/** 依赖工程 */
	private List<PatchProjectInfoDTO> dependencyProjectList;

	public PatchProjectDTO() {
		super();
	}

	public PatchProjectDTO(PatchProjectInfoDTO mainProject, List<PatchProjectInfoDTO> dependencyProjectList) {
		super();
		this.mainProject = mainProject;
		this.dependencyProjectList = dependencyProjectList;
	}

	public PatchProjectInfoDTO getMainProject() {
		return mainProject;
	}

	public void setMainProject(PatchProjectInfoDTO mainProject) {
		this.mainProject = mainProject;
	}

	public List<PatchProjectInfoDTO> getDependencyProjectList() {
		return dependencyProjectList;
	}

	public void setDependencyProjectList(List<PatchProjectInfoDTO> dependencyProjectList) {
		this.dependencyProjectList = dependencyProjectList;
	}

	@Override
	public String toString() {
		return "PatchProjectDTO [mainProject=" + mainProject + ", dependencyProjectList=" + dependencyProjectList + "]";
	}

}
