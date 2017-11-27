package com.luckytom.patch.model;

/**
 * svn、git等信息
 *
 * @author liyulin
 * @version 1.0 2017年11月5日 上午11:55:26
 */
public class TeamPluginDO {
	private TeamPluginType teamPluginType;
	private String serverUrl;
	private String username;
	private String password;

	public TeamPluginDO() {
		super();
	}

	public TeamPluginDO(TeamPluginType teamPluginType, String serverUrl, String username, String password) {
		super();
		this.teamPluginType = teamPluginType;
		this.serverUrl = serverUrl;
		this.username = username;
		this.password = password;
	}

	public TeamPluginType getTeamPluginType() {
		return teamPluginType;
	}

	public void setTeamPluginType(TeamPluginType teamPluginType) {
		this.teamPluginType = teamPluginType;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "TeamPluginDO [teamPluginType=" + teamPluginType + ", serverUrl=" + serverUrl + ", username=" + username
				+ ", password=" + password + "]";
	}

}
