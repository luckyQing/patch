package com.luckytom.patch.model;

import org.apache.maven.model.Model;

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
