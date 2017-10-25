package flibs.fson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FsonFileManagement {

	/**
	 * Ojo que sobreescribe archivos con el mismo nombre
	 * Usa buffered file writer
	 */
	public static void writeFsonFile(FSON fson, String path) throws IOException {
		File file = new File(path);
		if ( !file.exists() ) file.createNewFile();

		try (
			FileWriter fileWriter = new FileWriter( file.getAbsoluteFile() );
			BufferedWriter writer = new BufferedWriter(fileWriter);
		) {
			writer.write( fson.toString() );
		} catch (IOException e) {
			throw e;
		}
	}

	public static FSON loadFsonFile(String path) throws IOException {
		FSON flag = new FSON();

		File file = new File(path);

		try (
			FileReader fileReader = new FileReader(file.getAbsoluteFile());
			BufferedReader reader = new BufferedReader(fileReader);
		) {
			StringBuilder text = new StringBuilder("");
			String line;
			while((line=reader.readLine()) != null){
				text.append(line);
			}

			flag.loadFromString( text.toString() );
		} catch (IOException e) {
			throw e;
		}

		return flag;
	}
}
