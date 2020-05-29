package eu.tasgroup.hyperskill.jsonparser.file;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class FileManager {

	public String load(String path) throws IOException {
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
		return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
	}
	
	public File writeOnFile(String filePath, String stringToBeWritten) throws FileNotFoundException{

		File file = new File(filePath);
		try(FileWriter fw = new FileWriter(file);){
			fw.write(stringToBeWritten);
		}catch(IOException e){
		}
		
		return file;
	}
	
	
/*public void writeOnFile(String filePath, String stringToBeWritten) {
		
		try (PrintWriter out = new PrintWriter(filePath)) {
		    out.println(stringToBeWritten);
		}catch(IOException e){
		}
		
	}
*/}
