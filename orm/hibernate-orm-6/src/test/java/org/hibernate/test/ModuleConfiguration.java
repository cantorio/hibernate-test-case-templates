package org.hibernate.test;

public class ModuleConfiguration extends Identifier {

	private static final long serialVersionUID = 6875858016359420613L;

	private Settings settings;

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
