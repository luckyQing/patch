package com.luckytom.patch.util;

import com.luckytom.patch.runnable.ConsoleRunnable;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * ui控制台日志输出工具类
 *
 * @author luckytom
 * @version 1.0 2017年12月2日 下午6:04:35
 */
public final class ConsoleUtil {
	
	private static TextArea mLogTextArea;
	private static boolean mLogSwitch = true;

	public static void setLogTextArea(TextArea logTextArea) {
		mLogTextArea = logTextArea;
	}

	public static void setLogSwitch(boolean logSwitch) {
		mLogSwitch = logSwitch;
	}

	public static void info(String text) {
		if (mLogSwitch) {
			Platform.runLater(new ConsoleRunnable(mLogTextArea, text));
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void error(String text) {
		info(text);
		Thread.currentThread().stop();
	}

}
