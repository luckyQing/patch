package com.luckytom.patch.util;

import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.svn.core.SVNLogEntryPath;

public final class SvnPatchUtil {
	private static Map<String, String> fileChangeTypeMap;

	static {
		fileChangeTypeMap = new HashMap<String, String>(4);
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_ADDED), "新增");
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_MODIFIED), "修改");
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_DELETED), "删除");
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_REPLACED), "替换");
	}
}
