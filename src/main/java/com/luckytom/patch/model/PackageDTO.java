package com.luckytom.patch.model;

import com.luckytom.patch.constants.PatchDict;
import com.luckytom.patch.util.FileUtil;

/**
 * web项目信息
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午9:57:01
 */
public class PackageDTO {
	private String packagingName;
	private String version;
	private String packagingType;
	private volatile String compileJarName;

	public PackageDTO() {
		super();
	}

	public PackageDTO(String packagingName, String version, String packagingType) {
		super();
		this.packagingName = packagingName;
		this.version = version;
		this.packagingType = packagingType;
	}

	public String getPackagingName() {
		return packagingName;
	}

	public void setPackagingName(String packagingName) {
		this.packagingName = packagingName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackagingType() {
		return packagingType;
	}

	public void setPackagingType(String packagingType) {
		this.packagingType = packagingType;
	}

	public String getCompileJarName() {
		if (null == compileJarName) {
			if(PatchDict.ArchiveType.JAR.equals(packagingType)) {
				compileJarName = FileUtil.getCompileJarName(packagingName, version, packagingType);
			} else if(PatchDict.ArchiveType.WAR.equals(packagingType)) {
				compileJarName = FileUtil.getCompileWarName(packagingName, packagingType);
			}
		}
		return compileJarName;
	}
	
	
}
