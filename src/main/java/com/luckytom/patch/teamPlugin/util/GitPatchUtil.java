package com.luckytom.patch.teamPlugin.util;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luckytom.patch.model.LogFilterParam;
import com.luckytom.patch.model.TeamPluginDO;

public class GitPatchUtil {
	private static final Logger logger = LogManager.getFormatterLogger();

	/**
	 * 从git获取补丁文件列表
	 * 
	 * @param teamPlugin
	 * @param svnLogFilterParam
	 * @return
	 */
	public static List<String> getPatchList(TeamPluginDO teamPlugin, LogFilterParam logFilterParam) {
		return null;
	}

}
