package com.luckytom.patch.model;

/**
 * svn、git等信息
 *
 * @author luckytom
 * @version 1.0 2017年11月5日 上午11:55:26
 */
public class TeamPluginDO {
	private String serverUrl;
	private String username;
	private String password;

	public TeamPluginDO() {
		super();
	}

	public TeamPluginDO(String serverUrl, String username, String password) {
		super();
		this.serverUrl = serverUrl;
		this.username = username;
		this.password = password;
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
		return "TeamPluginDO [serverUrl=" + serverUrl + ", username=" + username + ", password=" + password + "]";
	}

}
