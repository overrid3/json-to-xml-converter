package eu.tasgroup.hyperskill.jsonparser.file;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileManager {

	public String load(String path) throws IOException {
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(path);
		return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
	}
}
