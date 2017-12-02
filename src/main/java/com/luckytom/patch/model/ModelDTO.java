package com.luckytom.patch.model;

import org.apache.maven.model.Model;

/**
 * pom.xml结构
 *
 * @author luckytom
 * @version 1.0 2017年12月2日 下午6:03:00
 */
public class ModelDTO {
	private Model model;
	private boolean reWrite;

	public ModelDTO() {
		super();
	}

	public ModelDTO(Model model, boolean reWrite) {
		super();
		this.model = model;
		this.reWrite = reWrite;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public boolean isReWrite() {
		return reWrite;
	}

	public void setReWrite(boolean reWrite) {
		this.reWrite = reWrite;
	}

}
