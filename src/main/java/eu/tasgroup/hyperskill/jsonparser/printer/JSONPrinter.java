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
            if (jsonE.getKey().startsWith("\"#") || !isNotLastChild(jsonE)) {
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
           printClosingParenthesis(jsonE);
            }
        
        return returnString;
    }

    private void printValueAndComma(JSONElement e) {

    	returnString += e.getValue() + ",\n";

    }

    private void printValue(JSONElement jsonE) {
    	returnString += jsonE.getValue() + "\n";
    }

    private boolean isNotLastChild(JSONElement jsonE) {
    	int size = jsonE.getParent().getChildren().size();
    	return size!=1 && !jsonE.equals(jsonE.getParent().getChildren().get(size-1));
    }
    private String chooseParenthesis(JSONElement jsonE) {
    	
    	return isNotLastChild(jsonE) ? "},\n" : "}\n";
    }
    
    private void printClosingParenthesis(JSONElement jsonE) {
    	returnString +=  (jsonE.getKey()!=null) ? chooseParenthesis(jsonE) : "}\n";
    }

    private void printElementKey(JSONElement jsonE) {
    	returnString +=  jsonE.getKey() + ": ";
    }

    private void printTabulation() {
        for (int i = 0; i < tabulation; i++) {
        	returnString += "\t";
        }
    }

    private void printParenthesis() {
    	returnString += "{\n";
    }
}
