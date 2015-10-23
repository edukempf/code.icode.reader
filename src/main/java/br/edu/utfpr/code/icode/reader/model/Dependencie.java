package br.edu.utfpr.code.icode.reader.model;

public class Dependencie {
	private Clazz dependence;
	private boolean external;
	
	public Dependencie(Clazz dependence, boolean external) {
		this.dependence = dependence;
		this.external = external;
	}
	
	
	public Clazz getDependence() {
		return dependence;
	}


	public void setDependence(Clazz dependence) {
		this.dependence = dependence;
	}


	public boolean isExternal() {
		return external;
	}
	public void setExternal(boolean external) {
		this.external = external;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (external ? 1231 : 1237);
		result = prime * result + ((dependence == null) ? 0 : dependence.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dependencie other = (Dependencie) obj;
		if (dependence == null) {
			if (other.dependence != null)
				return false;
		} else if (!dependence.equals(other.dependence))
			return false;
		return true;
	}
	
	
	
}
