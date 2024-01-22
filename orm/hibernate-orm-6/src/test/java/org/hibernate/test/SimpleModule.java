package org.hibernate.test;

public class SimpleModule extends Identifier {

	private static final long serialVersionUID = -2466303281239259377L;

	private ModuleConfiguration config;

	public ModuleConfiguration getConfig() {
		return config;
	}

	public void setConfig(ModuleConfiguration config) {
		this.config = config;
	}
}
