package com.luckytom.patch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * console命令行执行工具类
 *
 * @author liyulin
 * @version 1.0 2017年11月20日 下午9:54:00
 */
public final class CmdUtil {
	public static String systemEncoding;

	static {
		systemEncoding = System.getProperty("sun.jnu.encoding", "GBK");
	}

	public static boolean exeCmd(String cmd) {
		ConsoleUtil.info("exec==>" + cmd);

		Process process = null;
		boolean execState = false;
		try {
			process = Runtime.getRuntime().exec(cmd);

			execState = showNormalMsg(process) && showErrorMsg(process);
		} catch (Exception e) {
			ConsoleUtil.info(e.getMessage());
			execState = false;
		} finally {
			process.destroy();
		}
		return execState;
	}

	private static boolean showNormalMsg(Process process) {
		return showConsoleMsg(process.getInputStream());
	}

	private static boolean showErrorMsg(Process process) {
		if (process.exitValue() != 0) {
			showConsoleMsg(process.getErrorStream());
			return false;
		}
		return true;
	}

	private static boolean showConsoleMsg(InputStream inputStream) {
		try (InputStream in = inputStream; // 为了自动关闭InputStream
				Reader reader = new InputStreamReader(in, systemEncoding);
				BufferedReader bufferedReader = new BufferedReader(reader);) {
			String result = "";
			while ((result = bufferedReader.readLine()) != null) {
				ConsoleUtil.info(result);
			}
		} catch (IOException e) {
			ConsoleUtil.info(e.getMessage());
			return false;
		}
		return true;
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
	public static boolean exeCmds(String[] commands) {
		StringBuilder sb = new StringBuilder("cmd /c ");
		for (int i = 0, size = commands.length; i < size; i++) {
			if (i > 0) {
				sb.append(" & ");
			}
			sb.append(commands[i]);
		}

		return exeCmd(sb.toString());
	}
	
}
