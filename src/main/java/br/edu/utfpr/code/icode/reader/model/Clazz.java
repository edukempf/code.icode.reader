package br.edu.utfpr.code.icode.reader.model;

import java.util.ArrayList;

public class Clazz {
	
	private String address;
	private String parent;
	private String name;
	private ArrayList<Dependencie> interfaceDependencies;
	private ArrayList<Dependencie> extendsDependencies;
	private ArrayList<Dependencie> referenceDependencies;
	
	public Clazz(String address, String parent, String name) {
		this.address = address;
		this.parent = parent;
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Dependencie> getInterfaceDependencies() {
		return interfaceDependencies;
	}
	public void setInterfaceDependencies(ArrayList<Dependencie> interfaceDependencies) {
		this.interfaceDependencies = interfaceDependencies;
	}
	public ArrayList<Dependencie> getExtendsDependencies() {
		return extendsDependencies;
	}
	public void setExtendsDependencies(ArrayList<Dependencie> extendsDependencies) {
		this.extendsDependencies = extendsDependencies;
	}
	public ArrayList<Dependencie> getReferenceDependencies() {
		return referenceDependencies;
	}
	public void setReferenceDependencies(ArrayList<Dependencie> referenceDependencies) {
		this.referenceDependencies = referenceDependencies;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clazz other = (Clazz) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
	
	
	
	
	
}
