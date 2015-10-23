package br.edu.utfpr.code.icode.reader.model;

import java.util.ArrayList;

public class Component {
	private String name;
	private ArrayList<Clazz> clazzes;
	private ArrayList<Connector> requiredInternal;
	private ArrayList<Connector> requiredExternal;
	private ArrayList<Connector> providedInternal;
	private ArrayList<Connector> providedExternal;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Clazz> getClazzes() {
		return clazzes;
	}
	public void setClazzes(ArrayList<Clazz> clazzes) {
		this.clazzes = clazzes;
	}
	public ArrayList<Connector> getRequiredInternal() {
		return requiredInternal;
	}
	public void setRequiredInternal(ArrayList<Connector> requiredInternal) {
		this.requiredInternal = requiredInternal;
	}
	public ArrayList<Connector> getProvidedExternal() {
		return providedExternal;
	}
	public void setProvidedExternal(ArrayList<Connector> providedExternal) {
		this.providedExternal = providedExternal;
	}
	public ArrayList<Connector> getRequiredExternal() {
		return requiredExternal;
	}
	public void setRequiredExternal(ArrayList<Connector> requiredExternal) {
		this.requiredExternal = requiredExternal;
	}
	public ArrayList<Connector> getProvidedInternal() {
		return providedInternal;
	}
	public void setProvidedInternal(ArrayList<Connector> providedInternal) {
		this.providedInternal = providedInternal;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Component other = (Component) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
