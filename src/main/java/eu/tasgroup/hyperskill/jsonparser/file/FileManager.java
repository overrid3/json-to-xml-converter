package eu.tasgroup.hyperskill.jsonparser.file;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileManager {

	public String load(String path) throws IOException {
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
		return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
	}
	
	public File writeOnXMLFile(String filePath, String stringToBeWritten) throws IOException{

		File file = new File(filePath);
		try(FileWriter fw = new FileWriter(file);){
			fw.write(stringToBeWritten);
		}
		
		return file;
	}
	
}
