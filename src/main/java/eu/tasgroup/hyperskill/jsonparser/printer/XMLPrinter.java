package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.Objects;

import static eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor.XMLOBJECT_CANNOT_BE_NULL;

public class XMLPrinter {

    private int tabCount;
    private String returnString;

    public XMLPrinter() {
        tabCount=0;
        returnString = "";
    }

    public String printToXMLFormat(XMLElement e){

        Objects.requireNonNull(e, XMLOBJECT_CANNOT_BE_NULL);

        if(e.getTagName() != null){

            printTabulation();
            tabCount+=1;

            printTagName(e);
            printAttributes(e);
            
            if (e.getText()==null){
                printNullClosingTag();
            }
            else {
                printEnd(e);
                printText(e);
            }
        }

        e.getChildren().stream().forEach(child-> { printToXMLFormat(child); printNormalClosingTag(child); });
        tabCount-=1;
       return returnString;
        
    }

    private void printTagName(XMLElement e) {
    	returnString += "<"+e.getTagName();
    }

    private void printAttributes(XMLElement e) {
            e.getAttributes().entrySet().stream().forEach(el -> returnString += " " + el.getKey() + "=\"" + el.getValue()+"\"");//System.out.print(" " + el.getKey() + "=\"" + el.getValue()+"\""));
    }
    
    private void printEnd(XMLElement e) {
    	returnString += e.getChildren().isEmpty() ? ">" : ">\n";
    }

    private void printText(XMLElement e) {
    	returnString += e.getText();
    }

    private void printNullClosingTag() {
    	returnString += " />\n";
    }

    private void printNormalClosingTag(XMLElement e) {
        if (!e.getChildren().isEmpty()){
            printTabulation();
        }
        returnString += e.getText()!=null ? "</"+e.getTagName()+">\n" : "";
    }

    private void printTabulation() {
    	for(int i=0;i<tabCount; i++)
    		returnString +="\t";
    }

}