package br.edu.utfpr.code.icode.reader.model;

import java.util.ArrayList;

public class Clazz {
	
	private String address;
	private String parent;
	private String name;
	private ArrayList<String> dependencies;
	
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
	public ArrayList<String> getDependencies() {
		return dependencies;
	}
	public void setDependencies(ArrayList<String> dependencies) {
		this.dependencies = dependencies;
	}
	
	
}
