package br.edu.utfpr.code.icode.reader.model;

import java.util.ArrayList;

public class Clazz {
	
	private String address;
	private String parent;
	private String name;
	private ArrayList<Dependencie> interfaceDependencies;
	private ArrayList<Dependencie> extendsDependencies;
	private ArrayList<Dependencie> fieldsDependencies;
	private ArrayList<Dependencie> methodsDependencies;
	
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
	public ArrayList<Dependencie> getFieldsDependencies() {
		return fieldsDependencies;
	}
	public void setFieldsDependencies(ArrayList<Dependencie> fieldsDependencies) {
		this.fieldsDependencies = fieldsDependencies;
	}
	public ArrayList<Dependencie> getMethodsDependencies() {
		return methodsDependencies;
	}
	public void setMethodsDependencies(ArrayList<Dependencie> methodsDependencies) {
		this.methodsDependencies = methodsDependencies;
	}
	
	
}
