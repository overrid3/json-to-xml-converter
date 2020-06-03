package eu.tasgroup.hyperskill.jsonparser.utils;

import java.util.regex.Pattern;

public class XMLUtils {
	
	 static String regular="<\\/*(\\w+)((\\s\\w+\\s?=\\s?\"\\w+\")*)>?((}?>}?|.|\\r|\\n)+)\\s*";
	    static String openClosed="<(\\w+)((\\s\\w+\\s?=\\s?\"\\w+\")*)\\s?\\/>?((}?>}?|.|\\r|\\n)+)\\s*";
	    public static Pattern regularPattern=Pattern.compile(regular);
	    public static Pattern openClosedPattern=Pattern.compile(openClosed);

	    //SI POTEVA FARE ANCHE CON L'ENUM

	    public static String evaluateString(String xmlText){
	        if (xmlText.matches(regular)){
	            return "regular";
	        } else if (xmlText.matches(openClosed)){
	            return "openClosed";
	        }
	        return "tagValue";
	    }

}
