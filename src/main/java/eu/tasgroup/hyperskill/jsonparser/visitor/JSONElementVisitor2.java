package eu.tasgroup.hyperskill.jsonparser.visitor;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

import java.util.Map;
import java.util.Stack;

public class JSONElementVisitor2 {

    Stack<XMLElement> path;

    public JSONElementVisitor2(){
        path =new Stack<>();
    }

    public void visit(XMLElement e) {

        if (e.getTagName()!=null) {

            path.push(e);

            printPath(path);

            printValue(e);
            printAttributes(e);
        }

        for (XMLElement element: e.getChildren()){
            visit(element);
        }

        if (!path.isEmpty()) {
            path.pop();
        }

        else{
            //RETURN STRINGA;
        }
    }

    private void printPath(Stack<XMLElement> path) {

        System.out.print("\nPath = ");
        path.stream().filter(element -> path.indexOf(element)!=(path.size()-1)).forEach(el -> System.out.print(el.getTagName() + ", "));
        System.out.print(path.peek().getTagName());
        System.out.println();
    }

    private void printValue(XMLElement e) {

        if (e.getChildren().isEmpty()) {
            if (e.getText()==null)
                System.out.println("value = " + e.getText());
            else
                System.out.println("value = " +"\""  + e.getText() + "\"");
        }
    }

    private void printAttributes(XMLElement e){

        if (!e.getAttributes().isEmpty()){

            System.out.println("attributes:");
            for (Map.Entry entry: e.getAttributes().entrySet()) {
                System.out.println(entry.getKey() + " = " + "\""+entry.getValue()+"\"");
            }
        }
    }
}
