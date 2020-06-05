package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;

public class JSONPrinter {

    private String returnString;
    private int tabulation;

    public JSONPrinter() {
        tabulation = 0;
        returnString = "";
    }

    public String print(JSONElement jsonE) {
        if (jsonE.getKey() != null) {

            printTabulation();

            printElementKey(jsonE);
        }

        if (JSONUtils.hasNoChildren(jsonE)) {
            if (jsonE.getKey().startsWith("\"#")) {
                printValue(jsonE);
            } else {
                printValueAndComma(jsonE);
            }
        } else {
            printParenthesis();
            tabulation += 1;
            jsonE.getChildren().stream().forEach(el -> print(el));
            tabulation -= 1;
            printTabulation();
            printClosingParenthesis();
        }
        
        return returnString;
    }

    private void printValueAndComma(JSONElement e) {

    	returnString += e.getValue() + ",\n";
//        System.out.println(e.getValue() + ",");

    }

    private void printValue(JSONElement jsonE) {
    	returnString += jsonE.getValue() + "\n";
//        System.out.println(jsonE.getValue());
    }

    private void printClosingParenthesis() {
    	returnString += "}\n";
//        System.out.println("}");
    }

    private void printElementKey(JSONElement jsonE) {
    	returnString +=  jsonE.getKey() + ": ";
//        System.out.print(jsonE.getKey() + ": ");
    }

    private void printTabulation() {
        for (int i = 0; i < tabulation; i++) {
        	returnString += "\t";
//            System.out.print("\t");
        }
    }

    private void printParenthesis() {
    	returnString += "{\n";
//        System.out.println("{");
    }
}
