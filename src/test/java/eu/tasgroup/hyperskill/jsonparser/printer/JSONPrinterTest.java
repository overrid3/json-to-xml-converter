package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JSONPrinterTest {

    JSONPrinter sut=new JSONPrinter();

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
        
        String expected = "{\n" + 
        		"\t\"root\": {\n" + 
        		"\t\t\"id\": \"23424\",\n" + 
        		"\t\t\"number\": {\n" + 
        		"\t\t\t\"@region\": \"Russia\",\n" + 
        		"\t\t\t\"#number\": \"8-0-00-99-99\"\n" + 
        		"\t\t}\n" + 
        		"\t}\n" + 
        		"}\n";

        String actual = sut.print(first);
        assertThat(actual).isEqualTo(expected);
    }

}