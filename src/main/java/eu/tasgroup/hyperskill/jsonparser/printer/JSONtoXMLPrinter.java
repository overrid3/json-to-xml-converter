package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.Map;
import java.util.Stack;

public class JSONtoXMLPrinter {

    private Stack<XMLElement> s;
    private int tabCount;

    public JSONtoXMLPrinter() {
        s = new Stack<>();
        tabCount=0;
    }

    public void printToXMLFormat(XMLElement e) {

        if (e.getTagName() != null) {

            printTabs(tabCount);

            if (e.getText()==null) {
                System.out.print("<" + e.getTagName());
                printAttributes(e);
                System.out.println(" />");
            } else if (e.getText().equals("")) {
                System.out.print("<" + e.getTagName());
                printAttributes(e);
                System.out.println(">");
                s.push(e);
                tabCount += 1;
            } else  {
                System.out.print("<" + e.getTagName());
                printAttributes(e);
                System.out.print(">");
                System.out.print(e.getText());

                s.push(e);
                tabCount += 1;
            }
        }

        for (XMLElement child : e.getChildren()) {
            printToXMLFormat(child);
        }

        if (!s.isEmpty()) {
            System.out.println("</" + s.peek().getTagName() + ">");
            s.pop();
            tabCount -= 1;
        }

    }

    private void printTabs(int tabCount) {

        for (int i = 0; i < tabCount; i++) {
            System.out.print("\t");
        }

    }

    private void printAttributes(XMLElement e) {

        if (!e.getAttributes().isEmpty()) {
            for (Map.Entry<String,String> entry: e.getAttributes().entrySet()){
                System.out.print(" "+entry.getKey()+"="+entry.getValue());
            }
        }

    }
}

//NON FUNZIONA BENE SU EMPTY 2 PERCHE' LO VEDE COME ELEMENTO COMPLESSO