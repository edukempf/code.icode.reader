package br.edu.utfpr.code.icode.reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.github.javaparser.ParseException;

import br.edu.utfpr.code.icode.reader.control.ReaderCode;
import br.edu.utfpr.code.icode.reader.model.Clazz;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ReaderCode code = new ReaderCode("D:\\PROJETO EVOLUTEC\\Evolutec\\NeoNatal\\Sistema\\NeoNatal\\src\\br");
        try {
			code.readFiles("D:\\PROJETO EVOLUTEC\\Evolutec\\NeoNatal\\Sistema\\NeoNatal\\src\\br");
			ArrayList<String> pacotes = code.getPackages();
			ArrayList<Clazz> classes =  code.getClazzes();
			for (String p : pacotes) {
				System.out.println(p);
				for (Clazz clazz : classes) {
					if(clazz.getParent().equals(p)){
						String x ="";
						for (String ss : clazz.getDependencies()) {
							x += ss+", ";
						}
							System.out.println("----"+clazz.getName()+ " - "+x);
						
					}
				}
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
