package br.edu.utfpr.code.icode.reader.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import br.edu.utfpr.code.icode.reader.model.Clazz;

public class ReaderCode {

	private ArrayList<Clazz> clazzes;
	private ArrayList<String> packages;
	private String addressPackage;
	private String addressOriginal;
	private boolean first;
	private ArrayList<String> fieldDependencies;
	
	public ReaderCode(String projectAddress) {
		clazzes = new ArrayList<Clazz>();
		packages = new ArrayList<String>();
		addressPackage = "";
		first = true;
	}
	
	public ArrayList<Clazz> getClazzes() {
		return clazzes;
	}
	public ArrayList<String> getPackages() {
		return packages;
	}
	public void setFieldDependencies(ArrayList<String> fieldDependencies) {
		this.fieldDependencies = fieldDependencies;
	}

	public void readClass(File fileClazz, String initialDirectory){
		String packageName = fileClazz.getParent();
		packageName = packageName.replace(initialDirectory, "");
		packageName = packageName.replace("\\", ".");
		packageName = packageName.substring(1);
		Clazz clazz = new Clazz(fileClazz.getPath(), packageName, fileClazz.getName());
		clazzes.add(clazz);
		if(!packages.contains(packageName)){
			packages.add(packageName);
		}
	}
	
	
	
	public void readFiles(String projectAddress) throws ParseException, IOException{
		if(first){
			addressOriginal = projectAddress;
			first = false;
		}
		File file = new File(projectAddress);
		File[] files = file.listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].isDirectory()){
				readFiles(files[i].getPath());
			}else {
				String auxName = files[i].getName();
				if(auxName.contains(".java")){
					readClass(files[i], addressOriginal);
				}
			}
		}
		readDependencies();
	}
	
	private static class FieldsVerify extends VoidVisitorAdapter{
		ArrayList<String> dependencies = new ArrayList<String>();
		
		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(FieldDeclaration field, Object obj) {
			dependencies.add(field.getType().toString());
			
		}
	}
	
	private static class MethodDeclarationVerify extends VoidVisitorAdapter{
		ArrayList<String> dependencies = new ArrayList<String>();
		
		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(MethodDeclaration method, Object obj) {
			List<Parameter> parametersType = method.getParameters();
			if(parametersType != null)
			for (Parameter tp : parametersType) {
				System.out.println(tp.getType());
				dependencies.add(tp.getType().toString());
			}
		}
	}
	
	private static class VariablesVerify extends VoidVisitorAdapter{
		ArrayList<String> dependencies = new ArrayList<String>();
		
		public ArrayList<String> getDependencies() {
			return dependencies;
		}

		@Override
		public void visit(VariableDeclarationExpr variable, Object obj) {
			dependencies.add(variable.getType().toString());
		}
	}
	
	public void readDependencies() throws ParseException, IOException{
		ArrayList<String> dependencies, allDependencies;
		for (Clazz clazz : clazzes) {
			CompilationUnit comp = JavaParser.parse(new File(clazz.getAddress()));
			VariablesVerify variablesVerify = new  VariablesVerify();
			variablesVerify.visit(comp, null);
			FieldsVerify verifyFields = new FieldsVerify();
			verifyFields.visit(comp, null);
			MethodDeclarationVerify methodDeclarationVerify = new MethodDeclarationVerify();
			methodDeclarationVerify.visit(comp, null);
			
			dependencies = new  ArrayList<String>();
			allDependencies = new ArrayList<String>();
			
			allDependencies.addAll(verifyFields.getDependencies());
			allDependencies.addAll(variablesVerify.getDependencies());
			allDependencies.addAll(methodDeclarationVerify.getDependencies());
			
			for (String fieldDeclaration : allDependencies) {
				for (Clazz clazz2 : clazzes) {
					if(clazz2.getName().replace(".java", "").equals(fieldDeclaration))
						if(!dependencies.contains(fieldDeclaration))
							dependencies.add(fieldDeclaration);
				}
			}
			clazz.setDependencies(dependencies);
		}
	}
	
}
