package com.luckytom.patch.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc2.ISvnObjectReceiver;
import org.tmatesoft.svn.core.wc2.SvnLog;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnRevisionRange;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import com.luckytom.patch.model.SVNLogFilterParam;

public final class SvnPatchUtil {
	private static Map<String, String> fileChangeTypeMap;

	static {
		fileChangeTypeMap = new HashMap<String, String>(4);
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_ADDED), "新增");
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_MODIFIED), "修改");
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_DELETED), "删除");
		fileChangeTypeMap.put(String.valueOf(SVNLogEntryPath.TYPE_REPLACED), "替换");
	}
	private static final Logger logger = LogManager.getFormatterLogger();
	
	private static String svnRoot = "http://192.168.1.47:80/svn/product/dev/04_code/server/branches/V520_for_deploy/commbusi_api";  
	private static String svnUsername = "kangduo";
	private static String svnPassword = "kd_8855";
    public static final ThreadLocal<DateFormat> sdf = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    
    public static void auth() {
    	
    }
  
    public static void authSvn(SVNLogFilterParam svnLogFilterParam) throws SVNException {  
    	final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
    	 try {
             //svn 认证
             final SVNURL url = SVNURL.parseURIEncoded(svnRoot);
             svnOperationFactory.setAuthenticationManager(BasicAuthenticationManager.newInstance(svnUsername, svnPassword.toCharArray()));
             
             //日志查找
             SVNRevision startSVNRevision = SVNRevision.create(sdf.get().parse(svnLogFilterParam.getStartTime()));
             SVNRevision endSVNRevision = SVNRevision.create(sdf.get().parse(svnLogFilterParam.getEndTime()));
             SvnRevisionRange range = SvnRevisionRange.create(startSVNRevision, endSVNRevision);
             
             SvnLog log = svnOperationFactory.createLog();
             log.addRange(range);
             log.setDiscoverChangedPaths(true);
             log.setSingleTarget(SvnTarget.fromURL(url));

             //处理
             log.setReceiver(new ISvnObjectReceiver<SVNLogEntry>() {
                 public void receive(SvnTarget svnTarget, SVNLogEntry svnLogEntry) throws SVNException {
                     //每个版本执行一次，输出日志信息
                	 if(StringUtils.isNotBlank(svnLogFilterParam.getAuthor()) && !svnLogFilterParam.getAuthor().equals(svnLogEntry.getAuthor())) {
                		 return;
                	 }
                	 
                	 StringBuilder subLog = new StringBuilder(100);
                	 subLog.append("版本:").append(svnLogEntry.getRevision())
                	 	   .append("===作者：").append("svnLogEntry.getAuthor()")
                	 	   .append("===提交时间：").append(sdf.get().format(svnLogEntry.getDate()))
                	 	   .append("===备注:").append(svnLogEntry.getMessage());
                	 logger.info(subLog);
                	 
                     //处理变更
                     Map<String, SVNLogEntryPath> changedPathsMap = svnLogEntry.getChangedPaths();
                     if (changedPathsMap.size() > 0) {
                         for (Map.Entry<String, SVNLogEntryPath> entry : changedPathsMap.entrySet()) {
                             String key = entry.getKey();
                             SVNLogEntryPath path = changedPathsMap.get(key);
                             logger.info(fileChangeTypeMap.get(String.valueOf(path.getType())) + "：" + key);
                         }
                     }
                 }
             });
             log.run();
         } catch (Exception e) {
             logger.error(e.getMessage(), e);
         } finally {
             svnOperationFactory.dispose();
         }
    }  
    
    public static void main(String[] args) throws Exception {
    	String startTime = "2017-11-28 01:00:00";
    	String endTime="2017-11-28 16:00:00";
    	String author="yanghua";
    	
    	SVNLogFilterParam svnLogFilterParam = new SVNLogFilterParam(startTime, endTime, author);
    	
    	authSvn(svnLogFilterParam);
	}
  
}
