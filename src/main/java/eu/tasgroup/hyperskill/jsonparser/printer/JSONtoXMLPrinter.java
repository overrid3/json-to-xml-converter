package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.Objects;
import java.util.Stack;

import static eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor.XMLOBJECT_CANNOT_BE_NULL;

public class JSONtoXMLPrinter {

    private Stack<XMLElement> s;
    private int tabCount;
    private String returnString;

    public JSONtoXMLPrinter() {
        s = new Stack<>();
        tabCount=0;
        returnString="";
    }

    public void printToXMLFormat(XMLElement e){

        Objects.requireNonNull(e, XMLOBJECT_CANNOT_BE_NULL);

        if(e.getTagName() != null){

            printTabulation();
            tabCount+=1;
            //s.push(e);

            printTagName(e);
            printAttributes(e);

            if (e.getText()==null){
                printNullClosingTag();
            }
            else {
                printText(e);
            }
        }

        e.getChildren().stream().forEach(child-> { printToXMLFormat(child); printNormalClosingTag(child);});
        /*if (!s.isEmpty()){
            s.pop();
        }*/
        tabCount-=1;
    }

    private void printTagName(XMLElement e) {
    	//returnString += "<"+e.getTagName();
        System.out.print("<"+e.getTagName());
    }

    private void printAttributes(XMLElement e) {
        if (!e.getAttributes().isEmpty()){
            e.getAttributes().entrySet().stream().forEach(el ->System.out.print(" " + el.getKey() + "=" + el.getValue()));
        }

        if (e.getChildren().isEmpty()) {
            System.out.print(">");
        }

        else{
            System.out.println(">");
        }
    }

    private void printText(XMLElement e) {
    	//returnString += e.getText();
        System.out.print(e.getText());
    }

    private void printNullClosingTag() {
    	//returnString += " />\n";
        System.out.print(" />");
    }

    private void printNormalClosingTag(XMLElement e) {
    	//returnString += "</"+e.getTagName()+">\n";
        if (!e.getChildren().isEmpty()){
            printTabulation();
        }

        System.out.println("</"+e.getTagName()+">");
    }

    private void printTabulation() {
    	//returnString += "\n";
    	for(int i=0;i<tabCount; i++)
    		System.out.print("\t");
    }

}