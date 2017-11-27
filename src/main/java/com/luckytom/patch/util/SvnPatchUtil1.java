package com.luckytom.patch.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
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

public final class SvnPatchUtil1 {
	private static final String outputPath = null;
    //文件变更方式
    private static Map<String, String> typeDic = new HashMap<String, String>();
    //已处理的文件缓存 多次修改提交的重复文件跳过
    private static Map<String, String> cache = new HashMap<String, String>();
    //不用处理的文件，如jdbc
    private static List<String> exceptFileNames = null;
    private static List<String> projectToDeploy = new ArrayList<String>();

    private static final String LA = null;
    private static final String API = null;
    private static final String api_manager = null;
    private static final String Job = null;
    private static final String srcLA = null;
    private static final String srcAPI = null;
    private static final String srcApiManager = null;
    private static final String srcJob = null;

    private static boolean containsCommBusi = false;
    private static boolean containsDomainCommon = false;
    private static boolean containsServerCommon = false;
    private static boolean containsPom = false;

    static {
        typeDic.put("M", "修改");
        typeDic.put("A", "新增");
    }

    /**
     * 部署指定时间段或版本号的代码
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param startVersion 开始版本
     * @param endVersion 结束版本
     * @param projects 项目
     */
    protected static void svnDeploy(SVNRevision start, SVNRevision end, List<String> projects) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        final String svnUrl = null;
        final String svnUsername = null;
        final String svnPassword = null;
        final SvnOperationFactory svnOperationFactory = new SvnOperationFactory();

        projectToDeploy.addAll(projects);

        try {
			FileUtils.deleteDirectory(new File(outputPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("——————————————————清空（" + outputPath + "）目录下文件——————————————————\n");
        try {
            //svn 认证
            final SVNURL url = SVNURL.parseURIEncoded(svnUrl);
            svnOperationFactory.setAuthenticationManager(new BasicAuthenticationManager(svnUsername, svnPassword));

            //日志查找
            final SvnLog log = svnOperationFactory.createLog();
            log.addRange(SvnRevisionRange.create(start, end));
            log.setDiscoverChangedPaths(true);
            log.setSingleTarget(SvnTarget.fromURL(url));

            //处理
            log.setReceiver(new ISvnObjectReceiver<SVNLogEntry>() {
                public void receive(SvnTarget arg0, SVNLogEntry arg1) throws SVNException {
                    //每个版本执行一次，输出日志信息
                    System.out.print("版本:" + arg1.getRevision() + "===========作者：");
                    System.out.println(arg1.getAuthor() + "======时间：" + sdf.format(arg1.getDate()));
                    System.out.println("===提交日志:" + arg1.getMessage());
                    //处理变更
                    Map<String, SVNLogEntryPath> map = arg1.getChangedPaths();
                    if (map.size() > 0) {
                        for (Map.Entry<String, SVNLogEntryPath> entry : map.entrySet()) {
                            String key = entry.getKey();
                            SVNLogEntryPath path = map.get(key);
                            System.out.println(typeDic.get(path.getType() + "") + ":" + key);
                            handleFile(key);
                        }
                    }
                    System.out.println("\n");
                }
            });
            log.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            svnOperationFactory.dispose();
        }
        String commBusiJar = "\\WEB-INF\\lib\\commbusi_api-0.0.1-SNAPSHOT.jar";
        String serverCommJar = "\\WEB-INF\\lib\\server_common-0.0.3-SNAPSHOT.jar";
        String domainCommJar = "\\WEB-INF\\lib\\domain_common-0.0.1-SNAPSHOT.jar";
        if (containsCommBusi) {
            copyJar(commBusiJar);
        }
        if (containsDomainCommon) {
            copyJar(domainCommJar);
        }
        if (containsServerCommon) {
            copyJar(serverCommJar);
        }
        if (containsPom) {
            System.out.println("重要的事情说三遍！！！");
            System.out.println("pom.xml 文件已修改，请核对修改手动添加jar包到 WEB-INF/lib/ 下");
            System.out.println("pom.xml 文件已修改，请核对修改手动添加jar包到 WEB-INF/lib/ 下");
            System.out.println("pom.xml 文件已修改，请核对修改手动添加jar包到 WEB-INF/lib/ 下");
        }
    }

    private static void copyJar(String jarName) {
        if (projectToDeploy.contains("API")) {
            copyFile(srcAPI + jarName ,outputPath + API + jarName);
        }
        if (projectToDeploy.contains("LA")) {
            copyFile(srcLA + jarName ,outputPath + LA + jarName);
        }
        if (projectToDeploy.contains("api_manager")) {
            copyFile(srcApiManager + jarName ,outputPath + api_manager + jarName);
        }
        if (projectToDeploy.contains("Job")) {
            copyFile(srcJob + jarName ,outputPath + Job + jarName);
        }
    }
    /**
     * 处理文件
     */
    private static void handleFile(String path) {

        path = path.replace("main/java/", "");
        path = path.replace("main/resource/", "");
        boolean isFile = path.contains(".");
        //是否修改pom文件
        if (path.contains("pom.xml")) {
            containsPom = true;
        }
        if (path.contains("commbusi_api") && isFile) {
            containsCommBusi = true;
        }
        if (path.contains("domain_common") && isFile) {
            containsDomainCommon = true;
        }
        if (path.contains("server_common") && isFile) {
            containsServerCommon = true;
        }

        if (path.contains("api_manager") && projectToDeploy.contains("api_manager")) {
            dealSingleProject(path, api_manager, srcApiManager);
        } else if (path.contains("API") && projectToDeploy.contains("API")) {
            dealSingleProject(path, API, srcAPI);
        } else if (path.contains("Job") && projectToDeploy.contains("Job")) {
            dealSingleProject(path, Job, srcJob);
        } else if (path.contains("LA") && projectToDeploy.contains("LA")) {
            dealSingleProject(path, LA, srcLA);
        }
    }

    private static void dealSingleProject(String path, String projectName, String fromFilePath) {
        final String jspPath = null;
        final String src = null;
        final String javaPath = null;
        final String resourcePath = null;

        //是否是不用部署文件，可能误修改来的
        boolean isExcept = false;
        for (String name : exceptFileNames) {
            if (path.contains(name)) {
                isExcept = true;
            }
        }
        if (cache.get(path) == null && !isExcept) {
            if (path.contains(jspPath)) {
                //处理webapp下的文件
                String purePath = path.substring(path.lastIndexOf(jspPath) + jspPath.length());
                String srcPath = fromFilePath + "\\" + purePath.replace("/", "\\");
                String desPath = outputPath + projectName + "\\" + purePath.replace("/", "\\");
                copyFile(srcPath, desPath);
            } else if (path.contains(src)) {
                String purePath = path.substring(path.indexOf(src) + src.length());
                String srcPath = fromFilePath + "\\WEB-INF\\classes\\";
                String desPath = outputPath + projectName + "\\WEB-INF\\classes\\";
                if (path.toLowerCase().endsWith("java")) {
                    //处理java文件，复制class
                    String className = purePath.substring(purePath.lastIndexOf("/") + 1).replace(".java", "");
                    String classPath = purePath.substring(0, purePath.lastIndexOf("/") + 1).replace("/", "\\");
                    srcPath = srcPath + classPath.replace(javaPath, "");
                    desPath = desPath + classPath.replace(javaPath, "");
                    copyJava(srcPath, desPath, className);
                } else {
                    //处理xml及properties文件
                    srcPath = srcPath + purePath.replace("/", "\\").replace(resourcePath, "");
                    desPath = desPath + purePath.replace("/", "\\").replace(resourcePath, "");
                    copyFile(srcPath, desPath);
                }
            }
            cache.put(path, "1");//放到缓存
        }
    }

    /**
     * 复制普通文件
     */
    private static void copyFile(String src, String dest) {
        File destF = new File(dest);
        if (!destF.getParentFile().exists()) {
            boolean success = destF.getParentFile().mkdirs();
            if (!success) {
                System.err.println("文件创建失败！");
            }
        }
        File fsrc = new File(src);
        File fdest = new File(dest);
        if (fsrc.isDirectory()) {
            if (!fdest.exists()) {
                boolean success = fdest.mkdirs();
                if (!success) {
                    System.err.println("文件创建失败！");
                }
            }
        } else {
            try {
				FileUtils.copyFile(fsrc, fdest);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

    /**
     * 复制java文件，并处理含有内部类的
     */
    private static void copyJava(String src, String dest, String cname) { 
        File destF = new File(dest);
        if (!destF.exists()) {
            boolean success = destF.mkdirs();
            if (!success) {
                System.err.println("文件创建失败！");
            }
        }
        File srcF = new File(src);
        File[] files = srcF.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.getName().startsWith(cname)) {
                    //包含了内部类
                    String destPath1 = dest + file.getName();
                    try {
						FileUtils.copyFile(file, new File(destPath1));
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            }
        }
    }
}