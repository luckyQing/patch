package com.luckytom.patch.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luckytom.patch.constants.Resource;
import com.luckytom.patch.model.SettingDO;
import com.luckytom.patch.runnable.PatchRunnable;
import com.luckytom.patch.util.MavenUtil;

/**
 * 应用service
 * 
 * @author luckytom
 * @version 1.0 2017年11月23日 上午9:42:03
 */
public class PatchService {
	
	private static final Logger logger = LogManager.getFormatterLogger();
	private static ExecutorService patchSingleThreadExecutor = Executors.newSingleThreadExecutor();
	
	public static void generatePatch(SettingDO setting) {
		logger.info(Resource.CHECK_ENV);
		if (MavenUtil.EnvCheck.checkJDKEnv() && MavenUtil.EnvCheck.checkMavenEnv()) {
			patchSingleThreadExecutor.execute(new PatchRunnable(setting));
		}
		
	}
	
	public static void shutdownPatchSingleThreadExecutor() {
		if (null != patchSingleThreadExecutor && !patchSingleThreadExecutor.isShutdown()) {
			patchSingleThreadExecutor.shutdown();
		}
	}
	
}
