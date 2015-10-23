package br.edu.utfpr.code.icode.reader.model;

import java.util.ArrayList;

public class Structure {
	private ArrayList<Component> components;
	private ArrayList<ConnectorStructure> connectors;
	public ArrayList<Component> getComponents() {
		return components;
	}
	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}
	public ArrayList<ConnectorStructure> getConnectors() {
		return connectors;
	}
	public void setConnectors(ArrayList<ConnectorStructure> connectors) {
		this.connectors = connectors;
	}
	
	
}
