package br.edu.utfpr.code.icode.reader;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.javaparser.ParseException;

import br.edu.utfpr.code.icode.reader.control.StructureManager;
import br.edu.utfpr.code.icode.reader.model.Clazz;
import br.edu.utfpr.code.icode.reader.model.Component;
import br.edu.utfpr.code.icode.reader.model.Connector;
import br.edu.utfpr.code.icode.reader.model.ConnectorStructure;
import br.edu.utfpr.code.icode.reader.model.Structure;
import br.edu.utfpr.code.icode.reader.model.Tree;
import br.edu.utfpr.code.icode.reader.model.TypeConnection;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		StructureManager sm = new StructureManager();
		try {
			Structure s = sm.generateStructure("D:\\PROJETO EVOLUTEC\\Evolutec\\NeoNatal\\Sistema\\NeoNatal\\src\\br");
			
			
			System.out.println("ARQUITETURA");
			System.out.println("    COMPONENTES");
			for (Component c : s.getComponents()) {
				System.out.println("        " + c.getName());
				System.out.println("            CLASSES");
				for (Clazz c2 : c.getClazzes()) {
					System.out.println("                " + c2.getName());
				}
				System.out.println("            REQ INT INT");
				for (Connector c2 : c.getRequiredInternal()) {
					if (c2.getType() == TypeConnection.INTERFACE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_req_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            REQ INT EXT");
				for (Connector c2 : c.getRequiredExternal()) {
					if (c2.getType() == TypeConnection.INTERFACE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_req_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            REQ EXT INT");
				for (Connector c2 : c.getRequiredInternal()) {
					if (c2.getType() == TypeConnection.EXTENDS)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_req_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            REQ EXT EXT");
				for (Connector c2 : c.getRequiredExternal()) {
					if (c2.getType() == TypeConnection.EXTENDS)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_req_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            REQ REF INT");
				for (Connector c2 : c.getRequiredInternal()) {
					if (c2.getType() == TypeConnection.REFERENCE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_req_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            REQ REF EXT");
				for (Connector c2 : c.getRequiredExternal()) {
					if (c2.getType() == TypeConnection.REFERENCE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_req_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            PRO INT INT");
				for (Connector c2 : c.getProvidedInternal()) {
					if (c2.getType() == TypeConnection.INTERFACE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_pro_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            PRO INT EXT");
				for (Connector c2 : c.getProvidedExternal()) {
					if (c2.getType() == TypeConnection.INTERFACE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_pro_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            PRO EXT INT");
				for (Connector c2 : c.getProvidedInternal()) {
					if (c2.getType() == TypeConnection.EXTENDS)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_pro_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            PRO EXT EXT");
				for (Connector c2 : c.getProvidedExternal()) {
					if (c2.getType() == TypeConnection.EXTENDS)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_pro_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            PRO REF INT");
				for (Connector c2 : c.getProvidedInternal()) {
					if (c2.getType() == TypeConnection.REFERENCE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_pro_" + c2.getRequired().getName().replace(".java", ""));
				}
				System.out.println("            PRO REF EXT");
				for (Connector c2 : c.getProvidedExternal()) {
					if (c2.getType() == TypeConnection.REFERENCE)
						System.out.println("                " + c2.getProvided().getName().replace(".java", "") + "_pro_" + c2.getRequired().getName().replace(".java", ""));
				}
			}
			System.out.println("        CONECTORES");
			for(ConnectorStructure cs : s.getConnectors()){
				System.out.println("            " + cs.getName());
				System.out.println("                REQUIRED");
				System.out.println("                     " + cs.getConnector().getProvided().getName().replace(".java", "")+"_req_"+cs.getConnector().getRequired().getName().replace(".java", ""));
				System.out.println("                PROVIDED");
				System.out.println("                     " + cs.getConnector().getRequired().getName().replace(".java", "")+"_pro_"+cs.getConnector().getProvided().getName().replace(".java", ""));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
