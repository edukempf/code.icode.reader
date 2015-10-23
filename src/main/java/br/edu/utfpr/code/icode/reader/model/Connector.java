package br.edu.utfpr.code.icode.reader.model;

public class Connector {
	private Clazz provided;
	private Clazz required;
	private TypeConnection type;
	public Clazz getProvided() {
		return provided;
	}
	public void setProvided(Clazz provided) {
		this.provided = provided;
	}
	public Clazz getRequired() {
		return required;
	}
	public void setRequired(Clazz required) {
		this.required = required;
	}
	public TypeConnection getType() {
		return type;
	}
	public void setType(TypeConnection type) {
		this.type = type;
	}
	
}
