package com.luckytom.patch.util;

import com.luckytom.patch.runnable.LogRunnable;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

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
			Platform.runLater(new LogRunnable(mLogTextArea, text));
		}
	}

}
