package eu.tasgroup.hyperskill.jsonparser.app;

import java.io.IOException;

public class Main {

    public static void main(String args[]) throws IOException {
    	String path = "firstExample.json";
        String s = new DocumentConverter().analyzeAndConvert(path);
        System.out.println(s);
    }
}
