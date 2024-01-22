package org.hibernate.test;

public class ExtendedSettingsHolder extends Identifier {

	private static final long serialVersionUID = 6590955092727662627L;

	private ExtendedSettings extendedSettings;

	public ExtendedSettings getExtendedSettings() {
		return extendedSettings;
	}

	public void setExtendedSettings(ExtendedSettings extendedSettings) {
		this.extendedSettings = extendedSettings;
	}
}
