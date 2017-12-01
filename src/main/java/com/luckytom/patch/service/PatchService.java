package com.luckytom.patch.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.luckytom.patch.constants.Strings;
import com.luckytom.patch.model.SettingDO;
import com.luckytom.patch.runnable.PatchRunnable;
import com.luckytom.patch.util.MavenUtil;

/**
 * 应用service
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午9:42:03
 */
public class PatchService {
	private static final Logger logger = LogManager.getFormatterLogger();
	
	public static void generatePatch(SettingDO setting) {
		logger.info(Strings.CHECK_ENV);
		if (MavenUtil.EnvCheck.checkJDKEnv() && MavenUtil.EnvCheck.checkMavenEnv()) {
			Thread thread = new Thread(new PatchRunnable(setting));
			thread.setDaemon(true);
			thread.start();
		}
	}
	
}
