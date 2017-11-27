package com.luckytom.patch.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.luckytom.patch.model.ProjectDTO;

/**
 * maven工程工具类
 * 
 * @author liyulin
 * @version 1.0 2017年11月23日 上午10:10:57
 */
public class MavenProjectUtil {
	
	/**
	 * 获取项目信息
	 * 
	 * @param projectPath
	 * @return
	 */
	public static ProjectDTO getProjectInfo(String projectPath) {
		Model model = readModel(projectPath);

		String packagingType = model.getPackaging();
		String name = null;

		Build build = model.getBuild();
		if (null != build) {
			name = build.getFinalName();
		} else {
			name = model.getArtifactId();
		}

		return new ProjectDTO(name, packagingType);
	}
	
	/**
	 * 根据项目路径获取pom文件路径
	 * 
	 * @param projectPath
	 * @return
	 */
	public static String getPOMPath(String projectPath) {
		return projectPath + File.separator + "pom.xml";
	}

	/**
	 * 获取pom.xml结构
	 * 
	 * @param projectPath
	 * @return
	 */
	public static Model readModel(String projectPath) {
		MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
		Model model = null;
		try {
			model = mavenXpp3Reader.read(new FileReader(getPOMPath(projectPath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		return model;
	}

	/**
	 * 重写pom文件
	 * 
	 * @param projectPath
	 * @param model
	 */
	public static void writePOM(String projectPath, Model model) {
		MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
		try {
			mavenXpp3Writer.write(new FileWriter(getPOMPath(projectPath)), model);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
