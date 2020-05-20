package eu.tasgroup.hyperskill.jsonparser.traverse;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Traverser {

	Pattern elementPattern =  Pattern.compile("^\\s*\"([@?\\#?\\w\\.]*)\"\\s*:\\s*([\\w|\\.|\\s|-|'|\"]*),?((}?>}?|.|\\r|\\n)+)\\s*");
	Pattern closingElementPattern =  Pattern.compile("^\\s*\\}\\s*,?((?>}|.|\\r|\\n)*)");

	public JSONElement traverse(String json){
		JSONElement rootElement = new JSONElement();
		traverseBrutto(json.replaceFirst("\\{", ""), rootElement);
		return rootElement;
	}

	private void traverseBrutto(String json, JSONElement parentElement) {
		if(json.trim().isEmpty())
			return;

		// se siamo in presenza di un elemento generico
		Matcher elementWithValueMatcher = elementPattern.matcher(json);
		if (elementWithValueMatcher.find() ) {
			JSONElement element = new JSONElement();
			String elementKey = elementWithValueMatcher.group(1);
			String elementValue = elementWithValueMatcher.group(2);
			String remainString = elementWithValueMatcher.group(3);
			element.setKey(elementKey);

			if(parentElement != null)
				parentElement.addChild(element);
			element.setParent(parentElement);

			// se stiamo valutando un elemento complesso
			if (elementValue.trim().isEmpty() && remainString.trim().startsWith("{")){
				traverseBrutto(remainString.replaceFirst("\\{",""), element);
			} else { // stiamo valutando un elemento foglia (con un solo valore)
				element.setValue(elementValue);
				traverseBrutto(remainString, parentElement);
			}
			return;
		}
		// se siamo in presenza di una chiusura di un elemento complesso (})
		Matcher closingElementMatcher = this.closingElementPattern.matcher(json);
		if(closingElementMatcher.find()){
			String remainingString = closingElementMatcher.group(1);
			traverseBrutto(remainingString, parentElement.getParent());
			return;
		}

		throw new IllegalArgumentException("Json non valido");
	}


}
