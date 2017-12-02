package com.luckytom.patch.model;

/**
 * 第三方team工具类型
 *
 * @author luckytom
 * @version 1.0 2017年11月5日 上午11:55:59
 */
public enum TeamPluginType {
	SVN("svn"), GIT("git");
	private String name;

	TeamPluginType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static String[] getTeamPluginTypes() {
		TeamPluginType[] teamPluginTypeEnums = TeamPluginType.values();
		String[] teamPluginTypes = new String[teamPluginTypeEnums.length];
		for (int i = 0; i < teamPluginTypeEnums.length; i++) {
			teamPluginTypes[i] = teamPluginTypeEnums[i].getName();
		}

		return teamPluginTypes;
	}
}
