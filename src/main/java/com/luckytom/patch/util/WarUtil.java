package com.luckytom.patch.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * war文件工具类
 * 
 * @author luckytom
 * @version 1.0 2017年11月29日 下午3:30:58
 */
public final class WarUtil {
	private static final Logger logger = LogManager.getFormatterLogger();
	
	/**
	 * 解压war文件
	 * @param warPath
	 * @param unWarPath
	 */
	public static void unWar(String warPath, String unWarPath) {
		File warFile = new File(warPath);
		try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(warFile));
				ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, bufferedInputStream);) {
			ArchiveEntry entry = null;
			while (null != (entry = archiveInputStream.getNextEntry())) {
				if (entry.isDirectory()) {
					new File(unWarPath, entry.getName()).mkdir();
				} else {
					try (OutputStream out = FileUtils.openOutputStream(new File(unWarPath, entry.getName()));) {
						IOUtils.copy(archiveInputStream, out);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (ArchiveException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
