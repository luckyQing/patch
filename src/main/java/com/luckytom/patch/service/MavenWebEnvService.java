package com.luckytom.patch.service;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import com.luckytom.patch.model.ModelDTO;
import com.luckytom.patch.util.POMUtil;

/**
 * maven web环境处理
 *
 * @author liyulin
 * @version 1.0 2017年11月21日 上午11:43:44
 */
public class MavenWebEnvService {

	private static final String[][] JAR_POMS = new String[][] { { "javax.servlet", "jstl", "1.2", "provided" },
			{ "javax.servlet", "servlet-api", "2.5", "provided" } };

	private static final Dependency[] WEB_ENV_JARS = new Dependency[JAR_POMS.length];

	static {
		for (int i = 0; i < JAR_POMS.length; i++) {
			String[] jarPom = JAR_POMS[i];
			Dependency jstlDependency = new Dependency();
			jstlDependency.setGroupId(jarPom[0]);
			jstlDependency.setArtifactId(jarPom[1]);
			jstlDependency.setVersion(jarPom[2]);
			jstlDependency.setScope(jarPom[3]);

			WEB_ENV_JARS[i] = jstlDependency;
		}
	}

	/**
	 * <p>
	 * maven web环境pom初始化
	 * </p>
	 * 
	 * Note：如果maven web pom.xml中没有"jstl"、"servlet-api"等，执行“mvn clean package”时，<br>
	 * 会报错。
	 * 
	 * @param projectPath
	 */
	public static void initMavenWebEnv(String projectPath) {
		ModelDTO modelDTO = getModelDTO(projectPath);
		if (modelDTO.isReWrite()) {
			POMUtil.writePOM(projectPath, modelDTO.getModel());
		}
	}

	/**
	 * 获取pom文件结构
	 * 
	 * @param projectPath
	 * @return
	 */
	private static ModelDTO getModelDTO(String projectPath) {
		Model model = POMUtil.readModel(projectPath);

		ModelDTO modelDTO = new ModelDTO(model, false);
		List<Dependency> dependencyList = model.getDependencies();
		for (Dependency webEnvJar : WEB_ENV_JARS) {
			boolean isExist = false;
			for (Dependency dependency : dependencyList) {
				if (webEnvJar.getArtifactId().equals(dependency.getArtifactId())) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				modelDTO.setReWrite(true);
				model.addDependency(webEnvJar);
			}
		}
		return modelDTO;
	}

}
