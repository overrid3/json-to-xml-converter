package eu.tasgroup.hyperskill.jsonparser.printer;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import org.json.JSONException;

class JSONPrinterTest {

    JSONPrinter sut=new JSONPrinter();

    @DisplayName("prova di stampa")
    @Test
    public void test1() throws JSONException {

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
        JSONElement id= new JSONElement("\"id\"",23424);
        JSONElement number=new JSONElement("\"number\"","");
        JSONElement region=new JSONElement("\"@region\"","\"Russia\"");
        JSONElement numberT=new JSONElement("\"#number\"",null);

        first.addChild(root);
        root.addChild(id);
        root.addChild(number);
        number.addChild(region);
        number.addChild(numberT);
        
        String expected = "{" + 
        		"\"root\": {" + 
        		"\"id\": 23424," + 
        		"\"number\": {" + 
        		"\"@region\": \"Russia\"," + 
        		"\"#number\": null" + 
        		"}" + 
        		"}" + 
        		"}";

        String actual = sut.print(first);
        System.out.println(actual);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @DisplayName("prova di stampa 2")
    @Test
    public void test2() throws JSONException {

        /*{
    		"transaction": {
        		"id": "6753322",
        		"number": {
            		"@region": "Russia",
            		"#number": "8-900-000-00-00"
        		},
        		"amount": null
    		},
    		"meta": {
        		"version": 0.01
    		}
		}*/

        JSONElement root=new JSONElement(null,"");
        JSONElement transaction=new JSONElement("\"transaction\"","");
        JSONElement id= new JSONElement("\"id\"",23424);
        JSONElement number=new JSONElement("\"number\"","");
        JSONElement region=new JSONElement("\"@region\"","\"Russia\"");
        JSONElement numberT=new JSONElement("\"#number\"","\"8-900-000-00-00\"");
        JSONElement amount = new JSONElement("\"amount\"",null);
        JSONElement meta=new JSONElement("\"meta\"","");
        JSONElement version = new JSONElement("\"version\"",0.01);
        

        root.addChild(transaction);
        root.addChild(meta);
        transaction.addChild(id);
        transaction.addChild(number);
        transaction.addChild(amount);
        meta.addChild(version);
        number.addChild(region);
        number.addChild(numberT);
        
        String expected = "{" + 
        		"\"transaction\": {" + 
        		"\"id\": 23424," + 
        		"\"number\": {" + 
        		"\"@region\": \"Russia\"," + 
        		"\"#number\": \"8-900-000-00-00\"" + 
        		"}," + 
        		"\"amount\" : null " +
        		"}," +
        		"\"meta\" :{" +
        		"\"version\":0.01" +
        		"}"+
        		"}";

        String actual = sut.print(root);
        System.out.println(actual);
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

}