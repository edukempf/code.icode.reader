package br.edu.utfpr.code.icode.reader.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import br.edu.utfpr.code.icode.reader.model.Clazz;

public class ReaderCode {

	private ArrayList<Clazz> clazzes;
	private ArrayList<String> packages;
	private String addressOriginal;
	private boolean first;

	public ReaderCode(String projectAddress) {
		clazzes = new ArrayList<Clazz>();
		packages = new ArrayList<String>();
		first = true;
	}

	public ArrayList<Clazz> getClazzes() {
		return clazzes;
	}

	public ArrayList<String> getPackages() {
		return packages;
	}

	public void readClass(File fileClazz, String initialDirectory) {
		String packageName = fileClazz.getParent();
		packageName = packageName.replace(initialDirectory, "");
		packageName = packageName.replace("\\", ".");
		packageName = packageName.substring(1);
		Clazz clazz = new Clazz(fileClazz.getPath(), packageName, fileClazz.getName());
		clazzes.add(clazz);
		if (!packages.contains(packageName)) {
			packages.add(packageName);
		}
	}

	public void readFiles(String projectAddress) throws ParseException, IOException {
		if (first) {
			addressOriginal = projectAddress;
			first = false;
		}
		File file = new File(projectAddress);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				readFiles(files[i].getPath());
			} else {
				String auxName = files[i].getName();
				if (auxName.contains(".java")) {
					readClass(files[i], addressOriginal);
				}
			}
		}
		readDependencies();
	}

	private ArrayList<String> verifyGenenericDeclaration(String genericDeclaration){
		ArrayList<String> list = new ArrayList<String>();
		String[] text = genericDeclaration.split("<");
		for (int i = 0; i < text.length; i++) {
			if(text[i].contains(">"))
				text[i] = text[i].replace(">", "");
			list.add(text[i]);
		}
		return list;
	}
	
	private static class ClassDeclarionVerify extends VoidVisitorAdapter {
		ArrayList<String> dependencies = new ArrayList<String>();

		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(ClassOrInterfaceDeclaration declaration, Object obj) {
			List<ClassOrInterfaceType> heritage = declaration.getExtends();
			List<ClassOrInterfaceType> implement = declaration.getImplements();
			if (implement != null)
				for (ClassOrInterfaceType classOrInterfaceType : implement) {
					//System.out.println(classOrInterfaceType);
					dependencies.add(classOrInterfaceType.toString());
				}
			if (heritage != null)
				for (ClassOrInterfaceType classOrInterfaceType : heritage) {
					//System.out.println(classOrInterfaceType);
					dependencies.add(classOrInterfaceType.toString());
				}
		}
	}

	private static class FieldsVerify extends VoidVisitorAdapter {
		ArrayList<String> dependencies = new ArrayList<String>();

		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(FieldDeclaration field, Object obj) {
			dependencies.add(field.getType().toString());

		}
	}

	private static class MethodDeclarationVerify extends VoidVisitorAdapter {
		ArrayList<String> dependencies = new ArrayList<String>();

		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(MethodDeclaration method, Object obj) {
			List<Parameter> parametersType = method.getParameters();
			if (parametersType != null)
				for (Parameter tp : parametersType) {
					dependencies.add(tp.getType().toString());
				}
		}
	}

	private static class VariablesVerify extends VoidVisitorAdapter {
		ArrayList<String> dependencies = new ArrayList<String>();

		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(VariableDeclarationExpr variable, Object obj) {
			dependencies.add(variable.getType().toString());
		}
	}

	public void readDependencies() throws ParseException, IOException {
		ArrayList<String> dependencies, allDependencies;
		for (Clazz clazz : clazzes) {
			CompilationUnit comp = JavaParser.parse(new File(clazz.getAddress()));
			ClassDeclarionVerify declarationVerify = new ClassDeclarionVerify();
			declarationVerify.visit(comp, null);
			VariablesVerify variablesVerify = new VariablesVerify();
			variablesVerify.visit(comp, null);
			FieldsVerify verifyFields = new FieldsVerify();
			verifyFields.visit(comp, null);
			MethodDeclarationVerify methodDeclarationVerify = new MethodDeclarationVerify();
			methodDeclarationVerify.visit(comp, null);

			dependencies = new ArrayList<String>();
			allDependencies = new ArrayList<String>();

			allDependencies.addAll(verifyFields.getDependencies());
			allDependencies.addAll(variablesVerify.getDependencies());
			allDependencies.addAll(methodDeclarationVerify.getDependencies());
			allDependencies.addAll(declarationVerify.getDependencies());

			for (String dependencie : allDependencies) {
				ArrayList<String> dependecieGeneric = null;
				if(dependencie.contains("<"))
					dependecieGeneric = verifyGenenericDeclaration(dependencie);
				
				for (Clazz clazz2 : clazzes) {
					if (clazz2.getName().replace(".java", "").equals(dependencie))
						if (!dependencies.contains(dependencie) && !clazz.getName().replace(".java", "").equals(dependencie))
							dependencies.add(dependencie);
					if(dependecieGeneric != null){
						for (String string : dependecieGeneric) {
							if (!dependencies.contains(string) && clazz2.getName().replace(".java", "").equals(string))
								dependencies.add(string);
						}
					}
				}
			}
			clazz.setDependencies(dependencies);
		}
	}

}
