package org.hibernate.test;

import java.util.HashSet;
import java.util.Set;

public abstract class Settings extends Identifier {

	private static final long serialVersionUID = 8293537021798545858L;

	private String data;
	private Set<ModuleConfiguration> relatedConfigurations = new HashSet<>();

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Set<ModuleConfiguration> getRelatedConfigurations() {
		return relatedConfigurations;
	}

	public void setRelatedConfigurations(Set<ModuleConfiguration> relatedConfigurations) {
		this.relatedConfigurations = relatedConfigurations;
	}

}
