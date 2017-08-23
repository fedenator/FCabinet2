package flibs.fson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FsonFileManagement {
	/**
	 * Ojo que sobreescribe archivos con el mismo nombre
	 * Usa buffered file writer
	 */
	public static void writeFsonFile(FSON fson, String path) {
		try {
			File file = new File(path);
			if (!file.exists()) file.createNewFile();
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(fson.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FSON loadFsonFile(String path) {
		FSON flag = new FSON();
		
		try{
			File file = new File(path);
			FileReader fileReader = new FileReader(file.getAbsoluteFile());
			BufferedReader reader = new BufferedReader(fileReader);
			
			String text = "", line="";
			while((line=reader.readLine()) != null){
				text += line;
			}
			reader.close();
			
			flag.loadFromString(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}
