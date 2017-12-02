package com.luckytom.patch.util;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc2.SvnExport;
import org.tmatesoft.svn.core.wc2.SvnLog;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnRevisionRange;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import com.luckytom.patch.constants.Resource;
import com.luckytom.patch.model.SVNLogFilterParam;
import com.luckytom.patch.model.TeamPluginDO;

/**
 * svn工具类
 * 
 * @author luckytom
 * @version 1.0 2017年11月29日 上午10:23:31
 */
public final class SvnPatchUtil {
	
	private static final Logger logger = LogManager.getFormatterLogger();
	private static volatile Map<String, SvnOperationFactory> svnOperationFactoryCache = new ConcurrentHashMap<String, SvnOperationFactory>();
	private static Map<Character, String> fileChangeTypeMap;

	static {
		fileChangeTypeMap = new HashMap<Character, String>(4);
		fileChangeTypeMap.put(SVNLogEntryPath.TYPE_ADDED, Resource.TYPE_ADDED);
		fileChangeTypeMap.put(SVNLogEntryPath.TYPE_MODIFIED, Resource.TYPE_MODIFIED);
		fileChangeTypeMap.put(SVNLogEntryPath.TYPE_DELETED, Resource.TYPE_DELETED);
		fileChangeTypeMap.put(SVNLogEntryPath.TYPE_REPLACED, Resource.TYPE_REPLACED);

		initSvn();
	}

	public static void initSvn() {
		// 通过使用 http:// 和 https:// 访问
		DAVRepositoryFactory.setup();
		// 通过使用svn:// 和 svn+xxx://访问
		SVNRepositoryFactoryImpl.setup();
		// 通过使用file:///访问
		FSRepositoryFactory.setup();
	}

	/**
	 * 从svn获取补丁文件列表
	 * 
	 * @param teamPlugin
	 * @param svnLogFilterParam
	 * @return
	 */
	public static List<String> getPatchList(TeamPluginDO teamPlugin, SVNLogFilterParam svnLogFilterParam) {
		SvnOperationFactory svnOperationFactory = getSvnOperationFactory(teamPlugin.getUsername(), teamPlugin.getPassword());

		SvnRevisionRange svnRevisionRange = initSvnRevisionRange(svnLogFilterParam.getStartTime(), svnLogFilterParam.getEndTime());
		SvnTarget target = null;
		try {
			target = SvnTarget.fromURL(SVNURL.parseURIEncoded(teamPlugin.getServerUrl()));
		} catch (SVNException e) {
			logger.error(e.getMessage(), e);
		}
		List<SVNLogEntry> svnLogEntryList = getSVNLogEntryList(svnOperationFactory, svnRevisionRange, target, null);

		return getSubmitFileList(svnLogEntryList, svnLogFilterParam.getAuthor());
	}

	private static SvnRevisionRange initSvnRevisionRange(String startTime, String endTime) {
		SVNRevision startSVNRevision = null;
		SVNRevision endSVNRevision = null;
		try {
			if (StringUtils.isNotBlank(startTime)) {
				startSVNRevision = SVNRevision.create(DateUtil.SDF_YYYYMMDDHHMMSS.get().parse(startTime));
			}
			if (StringUtils.isNotBlank(endTime)) {
				endSVNRevision = SVNRevision.create(DateUtil.SDF_YYYYMMDDHHMMSS.get().parse(endTime));
			}

		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}

		return SvnRevisionRange.create(startSVNRevision, endSVNRevision);
	}

	private static SvnOperationFactory getSvnOperationFactory(String username, String password) {
		String key = String.valueOf(username.hashCode()) + String.valueOf(password.hashCode());
		SvnOperationFactory svnOperationFactory = svnOperationFactoryCache.get(key);
		if (null == svnOperationFactory) {
			BasicAuthenticationManager basicAuthenticationManager = BasicAuthenticationManager.newInstance(username, password.toCharArray());

			svnOperationFactory = new SvnOperationFactory();
			svnOperationFactory.setAuthenticationManager(basicAuthenticationManager);

			svnOperationFactoryCache.put(key, svnOperationFactory);
		}

		return svnOperationFactory;
	}

	public static void disposeAllSvnOperationFactory() {
		for (Map.Entry<String, SvnOperationFactory> entry : svnOperationFactoryCache.entrySet()) {
			SvnOperationFactory svnOperationFactory = entry.getValue();
			if (null != svnOperationFactory) {
				svnOperationFactory.dispose();
			}
		}
	}

	private static List<SVNLogEntry> getSVNLogEntryList(SvnOperationFactory svnOperationFactory, SvnRevisionRange range,
			SvnTarget target, Long limit) {
		List<SVNLogEntry> svnLogEntryList = new ArrayList<SVNLogEntry>();
		try {
			SvnLog log = svnOperationFactory.createLog();
			log.addRange(range);
			if (null != limit) {
				log.setLimit(limit);
			}
			log.setDiscoverChangedPaths(true);
			log.setSingleTarget(target);
			log.run(svnLogEntryList);
		} catch (SVNException e) {
			logger.error(e.getMessage(), e);
		}
		return svnLogEntryList;
	}

	/**
	 * 获取提交的文件列表
	 * 
	 * @param svnLogEntryList
	 * @param author
	 * @return
	 */
	private static List<String> getSubmitFileList(List<SVNLogEntry> svnLogEntryList, String author) {
		List<String> fileList = new ArrayList<String>();
		for (SVNLogEntry svnLogEntry : svnLogEntryList) {
			if (StringUtils.isNotBlank(author) && !author.equals(svnLogEntry.getAuthor())) {
				continue;
			}
			String subLog = String.format(Resource.SVN_LOG_DETAIL, svnLogEntry.getRevision(), svnLogEntry.getAuthor(),
					DateUtil.SDF_YYYYMMDDHHMMSS.get().format(svnLogEntry.getDate()), svnLogEntry.getMessage());
			logger.info(subLog);

			Map<String, SVNLogEntryPath> changedPathsMap = svnLogEntry.getChangedPaths();
			if (changedPathsMap.size() > 0) {
				for (Map.Entry<String, SVNLogEntryPath> entry : changedPathsMap.entrySet()) {
					String logDetail = String.format(Resource.SNV_OPERATION_LOG,
							fileChangeTypeMap.get(entry.getValue().getType()), entry.getKey());
					logger.info(logDetail);
				}

				fileList.addAll(changedPathsMap.keySet());
			}
		}

		return fileList;
	}

	/**
	 * check out老版本的pom.xml
	 * 
	 * @param teamPlugin
	 * @param saveUrl 保存的路径
	 * @return
	 */
	public static boolean exportOldPOM(TeamPluginDO teamPlugin, String saveUrl) {
		SvnOperationFactory svnOperationFactory = getSvnOperationFactory(teamPlugin.getUsername(), teamPlugin.getPassword());

		SvnTarget target = null;
		try {
			target = SvnTarget.fromURL(SVNURL.parseURIEncoded(teamPlugin.getServerUrl()));
		} catch (SVNException e) {
			logger.error(e.getMessage(), e);
		}
		Long oldVersion = getPOMLastVersion(svnOperationFactory, target);
		if (null == oldVersion) {
			return false;
		}

		exportFile(svnOperationFactory, oldVersion, target, saveUrl);

		return true;
	}

	/**
	 * 获取pom.xml的上一个版本号
	 * 
	 * @param svnOperationFactory
	 * @param target
	 * @return
	 */
	private static Long getPOMLastVersion(SvnOperationFactory svnOperationFactory, SvnTarget target) {
		SvnRevisionRange svnRevisionRange = initSvnRevisionRange(null, null);
		List<SVNLogEntry> svnLogEntryList = getSVNLogEntryList(svnOperationFactory, svnRevisionRange, target, 2L);
		if (null == svnLogEntryList || svnLogEntryList.size() != 2) {
			return null;
		}
		return svnLogEntryList.get(1).getRevision();
	}

	private static void exportFile(SvnOperationFactory svnOperationFactory, long version, SvnTarget source,
			String localUrl) {
		logger.info(String.format(Resource.EXPORT_FILE_START, localUrl));
		SvnExport svnExport = svnOperationFactory.createExport();
		svnExport.addTarget(SvnTarget.fromFile(new File(localUrl)));
		svnExport.setRevision(SVNRevision.create(version));
		svnExport.setSource(source);
		svnExport.setDepth(SVNDepth.INFINITY);
		svnExport.setForce(true);
		try {
			svnExport.run();
		} catch (SVNException e) {
			logger.error(e.getMessage(), e);
		}
		logger.info(String.format(Resource.EXPORT_FILE_END, localUrl));
	}

}
