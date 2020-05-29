package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import static eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor.XMLOBJECT_CANNOT_BE_NULL;

public class JSONtoXMLPrinter {

    private Stack<XMLElement> s;
    private int tabCount;

    public JSONtoXMLPrinter() {
        s = new Stack<>();
        tabCount=0;
    }

    public void printToXMLFormat(XMLElement e){

        Objects.requireNonNull(e, XMLOBJECT_CANNOT_BE_NULL);

        if (e.getTagName()!=null) {

            s.push(e);

            printTagName(e);
            printAttributes(e);

            if (e.getText()==null){
                printNullClosingTag();
            }

            else {
                printText(e);

                e.getChildren().stream().forEach(child -> printToXMLFormat(child));
                printNormalClosingTag(e);
            }

        }

    }

    private void printTagName(XMLElement e) {
        System.out.print("<"+e.getTagName());
    }

    private void printAttributes(XMLElement e) {
        if (!e.getAttributes().isEmpty()){
            e.getAttributes().entrySet().stream().forEach(el -> System.out.println(" " + el.getKey() + "=" + el.getValue()));
        }
    }

    private void printText(XMLElement e) {
        System.out.print(e.getText());
    }

    private void printNullClosingTag() {
        System.out.print(" />");
    }

    private void printNormalClosingTag(XMLElement e) {
        System.out.print("</"+e.getTagName()+">");
    }

}