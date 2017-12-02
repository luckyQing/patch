package com.luckytom.patch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * console命令行执行工具类
 *
 * @author luckytom
 * @version 1.0 2017年11月20日 下午9:54:00
 */
public final class CmdUtil {
	private static final Logger logger = LogManager.getFormatterLogger();
	public static String systemEncoding;

	static {
		systemEncoding = System.getProperty("sun.jnu.encoding", "GBK");
	}

	public static void exeCmd(String cmd) {
		logger.info("exec==>" + cmd);

		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);

			showNormalMsg(process);
			showErrorMsg(process);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			process.destroy();
		}
	}

	private static void showNormalMsg(Process process) {
		showConsoleMsg(process.getInputStream());
	}

	private static boolean showErrorMsg(Process process) {
		if (process.exitValue() != 0) {
			showConsoleMsg(process.getErrorStream());
			return false;
		}
		return true;
	}

	private static void showConsoleMsg(InputStream inputStream) {
		try (InputStream in = inputStream; // 为了自动关闭InputStream
				Reader reader = new InputStreamReader(in, systemEncoding);
				BufferedReader bufferedReader = new BufferedReader(reader);) {
			String result = "";
			while ((result = bufferedReader.readLine()) != null) {
				logger.info(result);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * 执行多个命令
	 * </p>
	 * 如：“cmd /c ping www.baidu.com && ipconfig”<br>
	 * “cmd /c E: & cd E:\jfq\code\V520_for_deploy\API & mvn clean package -Dmaven.test.skip=true”
	 * 
	 * @param commands
	 * @return
	 */
	public static void exeCmds(String[] commands) {
		StringBuilder sb = new StringBuilder("cmd /c ");
		for (int i = 0, size = commands.length; i < size; i++) {
			if (i > 0) {
				sb.append(" & ");
			}
			sb.append(commands[i]);
		}

		exeCmd(sb.toString());
	}
	
}
