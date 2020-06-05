package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.utils.JSONUtils;

public class XMLtoJSONPrinter {

    private String returnString;
    private int tabulation;

    public XMLtoJSONPrinter() {
        tabulation = 0;
        returnString = "";
    }

    public void print(JSONElement jsonE) {
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
    }

    private void printValueAndComma(JSONElement e) {

        System.out.println(e.getValue() + ",");

    }

    private void printValue(JSONElement jsonE) {
        System.out.println(jsonE.getValue());
    }

    private void printClosingParenthesis() {
        System.out.println("}");
    }

    private void printElementKey(JSONElement jsonE) {
        System.out.print(jsonE.getKey() + ": ");
    }

    private void printTabulation() {
        for (int i = 0; i < tabulation; i++) {
            System.out.print("\t");
        }
    }

    private void printParenthesis() {
        System.out.println("{");
    }
}
