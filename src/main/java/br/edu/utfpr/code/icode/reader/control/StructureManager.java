package br.edu.utfpr.code.icode.reader.control;

import java.io.IOException;
import java.util.ArrayList;

import com.github.javaparser.ParseException;

import br.edu.utfpr.code.icode.reader.model.Clazz;
import br.edu.utfpr.code.icode.reader.model.Component;
import br.edu.utfpr.code.icode.reader.model.Connector;
import br.edu.utfpr.code.icode.reader.model.ConnectorStructure;
import br.edu.utfpr.code.icode.reader.model.Dependencie;
import br.edu.utfpr.code.icode.reader.model.Structure;
import br.edu.utfpr.code.icode.reader.model.TypeConnection;

public class StructureManager {

	public ArrayList<Connector> generateConnectorsInternalProvided(Component component) {
		ArrayList<Connector> connectors = new ArrayList<Connector>();
		for (Connector con : component.getRequiredInternal()) {
			Connector pc = new Connector();
			pc.setProvided(con.getRequired());
			pc.setRequired(con.getProvided());
			pc.setType(con.getType());
			connectors.add(pc);
		}
		return connectors;
	}

	public ArrayList<Connector> generateConnectorsExternalProvided(Component component,
			ArrayList<Component> components) {
		ArrayList<Connector> connectors = new ArrayList<Connector>();
		for (Component comp : components) {
			if (!comp.equals(component)) {
				for (Connector conn : comp.getRequiredExternal()) {
					if (component.getClazzes().contains(conn.getRequired())) {
						//System.out.println("ewqeq");
						Connector pc = new Connector();
						pc.setProvided(conn.getRequired());
						pc.setRequired(conn.getProvided());
						pc.setType(conn.getType());
						connectors.add(pc);
					}
				}
			}
		}
		return connectors;
	}

	public ArrayList<Connector> generateConnectorsInternalRequired(Clazz clazz) {
		ArrayList<Connector> listConnectors = new ArrayList<Connector>();
		if (!clazz.getExtendsDependencies().isEmpty()) {
			for (Dependencie d : clazz.getExtendsDependencies()) {
				if (!d.isExternal()) {
					Connector c = new Connector();
					c.setRequired(d.getDependence());
					c.setProvided(clazz);
					c.setType(TypeConnection.EXTENDS);
					listConnectors.add(c);
				}
			}
		}
		if (!clazz.getInterfaceDependencies().isEmpty()) {
			for (Dependencie d : clazz.getInterfaceDependencies()) {
				if (!d.isExternal()) {
					Connector c = new Connector();
					c.setRequired(d.getDependence());
					c.setProvided(clazz);
					c.setType(TypeConnection.INTERFACE);
					listConnectors.add(c);
				}
			}
		}
		if (!clazz.getReferenceDependencies().isEmpty()) {
			for (Dependencie d : clazz.getReferenceDependencies()) {
				if (!d.isExternal()) {
					Connector c = new Connector();
					c.setRequired(d.getDependence());
					c.setProvided(clazz);
					c.setType(TypeConnection.REFERENCE);
					listConnectors.add(c);
				}
			}
		}
		return listConnectors;
	}

	public ArrayList<Connector> generateConnectorsExternalRequired(Clazz clazz) {
		ArrayList<Connector> listConnectors = new ArrayList<Connector>();
		if (!clazz.getExtendsDependencies().isEmpty()) {
			for (Dependencie d : clazz.getExtendsDependencies()) {
				if (d.isExternal()) {
					Connector c = new Connector();
					c.setRequired(d.getDependence());
					c.setProvided(clazz);
					c.setType(TypeConnection.EXTENDS);
					listConnectors.add(c);
				}
			}
		}
		if (!clazz.getInterfaceDependencies().isEmpty()) {
			for (Dependencie d : clazz.getInterfaceDependencies()) {
				if (d.isExternal()) {
					Connector c = new Connector();
					c.setRequired(d.getDependence());
					c.setProvided(clazz);
					c.setType(TypeConnection.INTERFACE);
					listConnectors.add(c);
				}
			}
		}
		if (!clazz.getReferenceDependencies().isEmpty()) {
			for (Dependencie d : clazz.getReferenceDependencies()) {
				if (d.isExternal()) {
					Connector c = new Connector();
					c.setRequired(d.getDependence());
					c.setProvided(clazz);
					c.setType(TypeConnection.REFERENCE);
					listConnectors.add(c);
				}
			}
		}
		return listConnectors;
	}

	public ArrayList<Component> generateComponents(ArrayList<String> packages, ArrayList<Clazz> clazzes) {
		ArrayList<Component> components = new ArrayList<Component>();
		for (String p : packages) {
			Component comp = new Component();
			comp.setName(p);
			ArrayList<Clazz> clazzesPackage = new ArrayList<Clazz>();
			ArrayList<Connector> internalRequired = new ArrayList<Connector>();
			ArrayList<Connector> externalRequired = new ArrayList<Connector>();
			for (Clazz clazz : clazzes) {
				// System.out.println(clazz.getName());
				if (clazz.getParent().equals(p)) {
					clazzesPackage.add(clazz);
					externalRequired.addAll(generateConnectorsExternalRequired(clazz));
					internalRequired.addAll(generateConnectorsInternalRequired(clazz));
				}
			}
			comp.setClazzes(clazzesPackage);
			comp.setRequiredExternal(externalRequired);
			//System.out.println(internalRequired.size() + "");
			comp.setRequiredInternal(internalRequired);
			components.add(comp);
		}
		for (Component c : components) {
			c.setProvidedInternal(generateConnectorsInternalProvided(c));
			c.setProvidedExternal(generateConnectorsExternalProvided(c, components));
		}
		return components;
	}

	public ArrayList<ConnectorStructure> generateAllConnectors(ArrayList<Component> components) {
		ArrayList<ConnectorStructure> connectors = new ArrayList<ConnectorStructure>();
		int i = 0;
		for (Component com : components) {
			for (Connector c : com.getRequiredInternal()) {
				ConnectorStructure cs = new ConnectorStructure();
				cs.setConnector(c);
				cs.setName("Connector_" + i);
				connectors.add(cs);
				i++;
			}
		}
		return connectors;
	}

	public Structure generateStructure(String projectAddress) throws ParseException, IOException {
		Structure structure = new Structure();
		ReaderCode code = new ReaderCode(projectAddress);
		code.readFiles(projectAddress);
		ArrayList<String> packages = code.getPackages();
		ArrayList<Clazz> clazzes = code.getClazzes();
		structure.setComponents(generateComponents(packages, clazzes));
		structure.setConnectors(generateAllConnectors(structure.getComponents()));
		return structure;
	}

	public Structure addComponent(Structure structure, Component component) {
		ArrayList<Component> comps = structure.getComponents();
		comps.add(component);
		structure.setComponents(comps);
		return structure;
	}

	public Structure splitComponent(Structure structure, Component component, ArrayList<Clazz> clazzesSplit) {
		Component c1 = new Component();
		Component c2 = new Component();
		ArrayList<Clazz> c1Clazzes = component.getClazzes();
		ArrayList<Connector> reqInt = new ArrayList<Connector>();
		ArrayList<Connector> reqExt = new ArrayList<Connector>();
		ArrayList<Connector> proInt = new ArrayList<Connector>();
		ArrayList<Connector> proExt = new ArrayList<Connector>();
		for (Clazz c : clazzesSplit) {
			c1Clazzes.remove(c);
		}
		for (Connector c : component.getRequiredInternal()) {
			if (c1Clazzes.contains(c.getProvided()))
				if (c1Clazzes.contains(c.getRequired())) {
					reqInt.add(c);
				} else {
					reqExt.add(c);
				}
		}
		for (Connector c : component.getRequiredExternal()) {
			if (c1Clazzes.contains(c.getProvided())) 
				reqExt.add(c);
		}
		
		for (Connector c : component.getProvidedExternal()) {
			if (c1Clazzes.contains(c.getProvided()))
				if (c1Clazzes.contains(c.getRequired())) {
					proInt.add(c);
				} else {
					proExt.add(c);
				}
		}
		for (Connector c : component.getRequiredExternal()) {
			if (c1Clazzes.contains(c.getProvided())) 
				proExt.add(c);
		}
		
		c1.setClazzes(c1Clazzes);
		c1.setName(component.getName()+"_pt1");
		c1.setProvidedExternal(proExt);
		c1.setProvidedInternal(proInt);
		c1.setRequiredExternal(reqExt);
		c1.setRequiredInternal(reqInt);
		
		reqInt = new ArrayList<Connector>();
		reqExt = new ArrayList<Connector>();
		proInt = new ArrayList<Connector>();
		proExt = new ArrayList<Connector>();
		for (Connector c : component.getRequiredInternal()) {
			if (clazzesSplit.contains(c.getProvided()))
				if (clazzesSplit.contains(c.getRequired())) {
					reqInt.add(c);
				} else {
					reqExt.add(c);
				}
		}
		for (Connector c : component.getRequiredExternal()) {
			if (clazzesSplit.contains(c.getProvided())) 
				reqExt.add(c);
		}
		
		for (Connector c : component.getProvidedExternal()) {
			if (clazzesSplit.contains(c.getProvided()))
				if (clazzesSplit.contains(c.getRequired())) {
					proInt.add(c);
				} else {
					proExt.add(c);
				}
		}
		for (Connector c : component.getRequiredExternal()) {
			if (clazzesSplit.contains(c.getProvided())) 
				proExt.add(c);
		}
		
		c2.setClazzes(clazzesSplit);
		c2.setName(component.getName()+"_pt2");
		c2.setProvidedExternal(proExt);
		c2.setProvidedInternal(proInt);
		c2.setRequiredExternal(reqExt);
		c2.setRequiredInternal(reqInt);
		
		structure = removeComponent(structure, component);
		structure = addComponent(structure, c1);
		structure = addComponent(structure, c2);
		
		return structure;
	}

	public Structure removeComponent(Structure structure, Component component) {
		ArrayList<Component> comps = structure.getComponents();
		comps.remove(component);
		structure.setComponents(comps);
		return structure;
	}

	public Structure mergeTwoComponents(Structure structure, Component component1, Component component2) {
		Component newComponent = new Component();
		ArrayList<Clazz> clazzes = new ArrayList<Clazz>();
		clazzes.addAll(component1.getClazzes());
		clazzes.addAll(component2.getClazzes());
		newComponent.setClazzes(clazzes);
		newComponent.setName(component1.getName() + "_merge_" + component2.getName());
		ArrayList<Connector> reqInt = new ArrayList<Connector>();
		ArrayList<Connector> reqExt = new ArrayList<Connector>();
		ArrayList<Connector> proInt = new ArrayList<Connector>();
		ArrayList<Connector> proExt = new ArrayList<Connector>();
		reqInt.addAll(component1.getRequiredInternal());
		reqInt.addAll(component2.getRequiredInternal());
		proInt.addAll(component1.getProvidedInternal());
		proInt.addAll(component2.getProvidedInternal());
		for (Connector c : component1.getRequiredExternal()) {
			if (component2.getClazzes().contains(c.getRequired()))
				reqInt.add(c);
			else
				reqExt.add(c);
		}
		for (Connector c : component2.getRequiredExternal()) {
			if (component1.getClazzes().contains(c.getRequired()))
				reqInt.add(c);
			else
				reqExt.add(c);
		}
		for (Connector c : component1.getProvidedExternal()) {
			if (component2.getClazzes().contains(c.getProvided()))
				proInt.add(c);
			else
				proExt.add(c);
		}
		for (Connector c : component2.getProvidedExternal()) {
			if (component1.getClazzes().contains(c.getProvided()))
				proInt.add(c);
			else
				proExt.add(c);
		}
		newComponent.setProvidedExternal(proExt);
		newComponent.setProvidedInternal(proInt);
		newComponent.setRequiredExternal(reqExt);
		newComponent.setRequiredInternal(reqInt);

		// int aux = structure.getComponents().indexOf(component1);
		ArrayList<Component> comps = structure.getComponents();
		comps.remove(component1);
		comps.remove(component2);
		comps.add(newComponent);
		structure.setComponents(comps);

		return structure;
	}

	public Structure moveClass(Structure structure, Component newLocation, Clazz clazz) {
		ArrayList<Component> comps = structure.getComponents();
		Component old = new Component();
		for (Component comp : comps) {
			if (comp.getName().equals(clazz.getParent()))
				old = comp;
		}
		int pos = comps.indexOf(old);
		comps.remove(old);
		ArrayList<Connector> connInt = old.getRequiredInternal();
		ArrayList<Connector> connExt = old.getRequiredExternal();
		ArrayList<Connector> proInt = old.getRequiredInternal();
		ArrayList<Connector> proExt = old.getRequiredExternal();
		ArrayList<Clazz> clazzes = old.getClazzes();
		clazzes.remove(clazz);
		for (Connector connector : connInt) {
			if (connector.getRequired().equals(clazz)) {
				connExt.add(connInt.remove(connInt.indexOf(connector)));
			}
		}

		for (Connector connector : proInt) {
			if (connector.getProvided().equals(clazz)) {
				proExt.add(proInt.remove(proInt.indexOf(connector)));
			}
		}
		old.setClazzes(clazzes);
		old.setProvidedExternal(proExt);
		old.setProvidedInternal(proInt);
		old.setRequiredExternal(connExt);
		old.setRequiredInternal(connInt);
		comps.add(pos, old);

		pos = comps.indexOf(newLocation);
		comps.remove(newLocation);
		ArrayList<Clazz> clazzesNew = newLocation.getClazzes();
		clazzesNew.add(clazz);
		connInt = newLocation.getRequiredInternal();
		connExt = newLocation.getRequiredExternal();
		proExt = newLocation.getRequiredExternal();
		proInt = newLocation.getRequiredInternal();
		for (Connector connector : connExt) {
			if (connector.getRequired().equals(clazz)) {
				connInt.add(connExt.remove(connExt.indexOf(connector)));
			}
		}

		for (Connector connector : proExt) {
			if (connector.getProvided().equals(clazz)) {
				proInt.add(proExt.remove(proExt.indexOf(connector)));
			}
		}
		newLocation.setProvidedExternal(proExt);
		newLocation.setProvidedInternal(proInt);
		newLocation.setRequiredExternal(connExt);
		newLocation.setRequiredInternal(connInt);
		comps.add(pos, newLocation);

		structure.setComponents(comps);

		return structure;
	}

}
