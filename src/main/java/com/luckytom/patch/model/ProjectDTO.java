package com.luckytom.patch.model;

/**
 * web项目信息
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午9:57:01
 */
public class ProjectDTO {
	private String name;
	private String packagingType;

	public ProjectDTO() {
		super();
	}

	public ProjectDTO(String name, String packagingType) {
		super();
		this.name = name;
		this.packagingType = packagingType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackagingType() {
		return packagingType;
	}

	public void setPackagingType(String packagingType) {
		this.packagingType = packagingType;
	}

}
