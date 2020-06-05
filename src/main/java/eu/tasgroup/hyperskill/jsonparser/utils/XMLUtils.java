package eu.tasgroup.hyperskill.jsonparser.utils;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.regex.Pattern;

public class XMLUtils {
	
	 static String regular="^<\\/*(\\w+)((\\s\\w+=\"\\w+\")*)>((}?>}?|.|\\r|\\n)*)";
	    static String openClosed="^<(\\w+)((\\s\\w+=\"\\w+\")*)\\s?\\/>((}?>}?|.|\\r|\\n)*)";
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

	    public static boolean hasNoChildren(XMLElement e){
	    	return e.getChildren().isEmpty();
		}

}
