package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class XMLtoJSONPrinterTest {

    XMLtoJSONPrinter sut=new XMLtoJSONPrinter();

    @DisplayName("prova di stampa")
    @Test
    public void test1() {

        /*{
            "root": {
            "id": "6753322",
                    "number": {
                   "@region": "Russia",
                   "#number": "8-900-000-00-00"
                },
            "nonattr1": null
            }
        }*/

        JSONElement first=new JSONElement(null,"");
        JSONElement root=new JSONElement("\"root\"","");
        JSONElement id= new JSONElement("\"id\"","\"23424\"");
        JSONElement number=new JSONElement("\"number\"","");
        JSONElement region=new JSONElement("\"@region\"","\"Russia\"");
        JSONElement numberT=new JSONElement("\"#number\"","\"8-0-00-99-99\"");

        first.addChild(root);
        root.addChild(id);
        root.addChild(number);
        number.addChild(region);
        number.addChild(numberT);

        sut.print(first);
    }

}