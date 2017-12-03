package com.luckytom.patch.runnable;

import javafx.scene.control.TextArea;

/**
 * 日志runnable
 * 
 * @author luckytom
 * @version 1.0 2017年11月23日 上午11:11:29
 */
public class ConsoleRunnable implements Runnable {
	
	private TextArea mLogTextArea;
	private String mContent;

	public ConsoleRunnable(TextArea logTextArea, String content) {
		this.mLogTextArea = logTextArea;
		this.mContent = content;
	}

	@Override
	public void run() {
		mLogTextArea.appendText(mContent + "\n");
	}

}
