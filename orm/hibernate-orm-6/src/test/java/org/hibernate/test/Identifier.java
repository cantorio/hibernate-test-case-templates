package org.hibernate.test;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.Hibernate;

public class Identifier implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj != null && Hibernate.getClass(obj).equals(Hibernate.getClass(this))) {
			Identifier other = (Identifier) obj;
			return Objects.equals(getId(), other.getId());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
