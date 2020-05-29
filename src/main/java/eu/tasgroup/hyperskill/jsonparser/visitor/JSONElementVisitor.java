package eu.tasgroup.hyperskill.jsonparser.visitor;

import java.util.Objects;
import java.util.Stack;

import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;

public class JSONElementVisitor {

	public static String XMLOBJECT_CANNOT_BE_NULL = "XML Object cannot be null";
	    Stack<XMLElement> path;
	    String stringa = "";
	    public JSONElementVisitor(){
	        path =new Stack<>();
	    }

	    public String  createString (XMLElement e) {
	    	visit(e);
	    	return stringa;
	    }
	    public void visit(XMLElement e) {
	    	
	    	Objects.requireNonNull(e, XMLOBJECT_CANNOT_BE_NULL);

	        if (e.getTagName()!=null) {

	            path.push(e);

	            printPath(path);
	            printValue(e);
	            printAttributes(e);
	        }

	        e.getChildren().stream().forEach(child -> visit(child));

	        if (!path.isEmpty()) {
	            path.pop();
	        }
	    }

	    private void printPath(Stack<XMLElement> path) {

	    	stringa += "\nElement:\npath = ";
	        path.stream().filter(element -> path.indexOf(element)!=(path.size()-1)).forEach(el -> stringa += el.getTagName() + ", ");
	        stringa+= path.peek().getTagName() + "\n";
	    }

	    private void printValue(XMLElement e) {

	        if (e.getChildren().isEmpty()) 
	        	stringa += e.getText()==null ? "value = " + e.getText()+"\n" : "value = " +"\""  + e.getText() + "\"\n";
	    }

	    private void printAttributes(XMLElement e){

	        if (!e.getAttributes().isEmpty()){
	            stringa += "attributes:\n";
	            e.getAttributes().entrySet().stream().forEach(entry -> stringa += (entry.getKey() + " = " + "\""+entry.getValue()+"\"\n"));
	        }
	    }
	}



