package br.edu.utfpr.code.icode.reader.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import br.edu.utfpr.code.icode.reader.model.Dependencie;

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

	private ArrayList<String> verifyGenenericDeclaration(String genericDeclaration) {
		ArrayList<String> list = new ArrayList<String>();
		String[] text = genericDeclaration.split("<");
		for (int i = 0; i < text.length; i++) {
			if (text[i].contains(">"))
				text[i] = text[i].replace(">", "");
			list.add(text[i]);
		}
		return list;
	}

	private static class ClassDeclarionVerify extends VoidVisitorAdapter {
		private ArrayList<String> extendsDependencies = new ArrayList<String>();
		private ArrayList<String> implementsDependencies = new ArrayList<String>();

		public ArrayList<String> getExtendsDependencies() {
			return extendsDependencies;
		}

		public ArrayList<String> getImplementsDependencies() {
			return implementsDependencies;
		}

		@Override
		public void visit(ClassOrInterfaceDeclaration declaration, Object obj) {
			List<ClassOrInterfaceType> heritage = declaration.getExtends();
			List<ClassOrInterfaceType> implement = declaration.getImplements();
			if (implement != null)
				for (ClassOrInterfaceType classOrInterfaceType : implement) {
					// System.out.println(classOrInterfaceType);
					implementsDependencies.add(classOrInterfaceType.toString());
				}
			if (heritage != null)
				for (ClassOrInterfaceType classOrInterfaceType : heritage) {
					// System.out.println(classOrInterfaceType);
					extendsDependencies.add(classOrInterfaceType.toString());
				}
		}
	}

	private static class FieldsVerify extends VoidVisitorAdapter {
		private ArrayList<String> dependencies = new ArrayList<String>();

		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(FieldDeclaration field, Object obj) {
			dependencies.add(field.getType().toString());

		}
	}

	private static class MethodDeclarationVerify extends VoidVisitorAdapter {
		private ArrayList<String> dependencies = new ArrayList<String>();

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
		private ArrayList<String> dependencies = new ArrayList<String>();

		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(VariableDeclarationExpr variable, Object obj) {
			dependencies.add(variable.getType().toString());
		}
	}

	public ArrayList<Dependencie> generateListDependencies(ArrayList<String> geralList, Clazz clazz) {
		ArrayList<Dependencie> dependencies = new ArrayList<Dependencie>();
		for (String dependencieName : geralList) {
			ArrayList<String> dependecieGeneric = null;
			if (dependencieName.contains("<"))
				dependecieGeneric = verifyGenenericDeclaration(dependencieName);

			for (Clazz clazz2 : clazzes) {
				Dependencie dependencie;
				if (clazz2.getName().replace(".java", "").equals(dependencieName)){
					if (clazz.getParent().equals(clazz2.getParent()))
						dependencie = new Dependencie(dependencieName, false);
					else
						dependencie = new Dependencie(dependencieName, true);
					if (!dependencies.contains(dependencie)
							&& !clazz.getName().replace(".java", "").equals(dependencieName))
						dependencies.add(dependencie);
				}
				if (dependecieGeneric != null) {
					for (String string : dependecieGeneric) {
						Dependencie genericDependencie;
						if (clazz.getParent().equals(clazz2.getParent()))
							genericDependencie = new Dependencie(string, false);
						else
							genericDependencie = new Dependencie(string, true);
						if (!dependencies.contains(genericDependencie) && clazz2.getName().replace(".java", "").equals(string))
							dependencies.add(genericDependencie);
					}
				}
			}
		}
		return dependencies;
	}

	public void readDependencies() throws ParseException, IOException {
		ArrayList<String> dependencies, allDependenciesFields;
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
			
			allDependenciesFields = new ArrayList<String>();

			allDependenciesFields.addAll(verifyFields.getDependencies());
			allDependenciesFields.addAll(variablesVerify.getDependencies());

			clazz.setExtendsDependencies(generateListDependencies(declarationVerify.getExtendsDependencies(), clazz));
			clazz.setInterfaceDependencies(generateListDependencies(declarationVerify.getImplementsDependencies(), clazz));
			clazz.setFieldsDependencies(generateListDependencies(allDependenciesFields, clazz));
			clazz.setMethodsDependencies(generateListDependencies(methodDeclarationVerify.getDependencies(), clazz));
		}
	}

}
