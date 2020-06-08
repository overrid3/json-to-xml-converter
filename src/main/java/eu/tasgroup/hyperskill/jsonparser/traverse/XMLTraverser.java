package eu.tasgroup.hyperskill.jsonparser.traverse;


import java.util.*;
import java.util.regex.Matcher;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.utils.XMLUtils;

public class XMLTraverser {

    private final String XMLTEXT_CAN_T_BE_NULL ="Xml text can't be null or empty" ;

    private final Stack<XMLElement> tagsForPath;

    public XMLTraverser(){
        tagsForPath=new Stack<>();
    }

    //IMPORTA ORG.APACHE.COMMONS

    public XMLElement traverse(String xmlText){

        Objects.requireNonNull(xmlText, XMLTEXT_CAN_T_BE_NULL);

        String res= XMLUtils.evaluateString(xmlText);

        Matcher m;
        if (res.equals("regular")){

            m =XMLUtils.regularPattern.matcher(xmlText);

            if (m.find()) {

                XMLElement e;
                if (tagsForPath.isEmpty()){
                    e=buildElement(m.group(1), m.group(2).trim());
                    tagsForPath.push(e);
                }

                else {
                    if (m.group(1).equals(tagsForPath.peek().getTagName())) {

                        if (tagsForPath.size()==1){
                            return tagsForPath.peek();
                        }

                        tagsForPath.pop();
                    }

                    else {
                       e=buildElement(m.group(1), m.group(2).trim());
                       tagsForPath.peek().addChild(e);
                       tagsForPath.push(e);
                    }
                }
            }
            return traverse(xmlText.substring(xmlText.indexOf(">")+1).trim());
        }

        else if (res.equals("openClosed"))  {

            m =XMLUtils.openClosedPattern.matcher(xmlText);

            if (m.find()){
                XMLElement e=buildElement(m.group(1), m.group(2).trim());
                e.setText("null"); //SOSTITUIRE CON NULL (NON STRINGA)
                tagsForPath.peek().addChild(e);
            }
            return traverse(xmlText.substring(xmlText.indexOf(">")+1).trim());
        }


        tagsForPath.peek().setText(xmlText.substring(0,xmlText.indexOf("<")));
        return traverse(xmlText.substring(xmlText.indexOf("<")).trim());
    }


    private XMLElement buildElement(String tagName, String attributes) {

        XMLElement element=new XMLElement(tagName);

        if(!attributes.isEmpty()) {
        String str=attributes.replaceAll("\\s?=\\s?","=");
        
        String[] strArray = str.split(" ");
        for (int i = 0; i < strArray.length; i++) {

            String data = strArray[i];

            String[] keyValue = data.split("=");

            element.insertAttributeEntry(keyValue[0], keyValue[1].replace("\"", ""));

        }
    	}

        return element;
    }
}
