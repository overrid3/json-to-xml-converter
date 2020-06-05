package eu.tasgroup.hyperskill.jsonparser.app;

import eu.tasgroup.hyperskill.jsonparser.converter.XMLToJSONConverter;
import eu.tasgroup.hyperskill.jsonparser.file.FileManager;
import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.printer.JSONtoXMLPrinter;
import eu.tasgroup.hyperskill.jsonparser.printer.XMLtoJSONPrinter;
import eu.tasgroup.hyperskill.jsonparser.traverse.JSONTraverser;
import eu.tasgroup.hyperskill.jsonparser.converter.JSONToXMLConverter;
import eu.tasgroup.hyperskill.jsonparser.traverse.XMLTraverser;

import java.io.File;
import java.io.IOException;

public class DocumentConverter {

    private FileManager fm;

    private JSONTraverser jsonTrv;
    private JSONToXMLConverter jsonConverter;
    private JSONtoXMLPrinter jsonPrinter;

    private XMLTraverser xmlTrv;
    private XMLToJSONConverter xmlConverter;
    private XMLtoJSONPrinter xmlPrinter;

    public DocumentConverter() {

        fm=new FileManager();

        jsonTrv =new JSONTraverser();
        jsonConverter=new JSONToXMLConverter();
        jsonPrinter =new JSONtoXMLPrinter();

        xmlTrv=new XMLTraverser();
        xmlConverter=new XMLToJSONConverter();
        xmlPrinter=new XMLtoJSONPrinter();
    }

    public void convertJsonToXml() throws IOException {

        String JSONDestinationPath="C:\\Users\\donatom\\IdeaProjects\\BertucciolisConverter\\src\\test\\resources\\jsontext.txt";

        String text=fm.load("first.txt");

        if (text.startsWith("{")) {

            JSONElement jsonE= jsonTrv.traverse(text);

            XMLElement xmlE=jsonConverter.convert(jsonE);

            String jsonToXml = jsonPrinter.printToXMLFormat(xmlE);

            File file = fm.writeOnXMLFile(JSONDestinationPath, jsonToXml);
        }

        else {

            XMLElement xmlE=xmlTrv.traverse(text);

            JSONElement jsonE=xmlConverter.convert(xmlE);

            System.out.println(jsonE.getKey());

            //String xmlToJson=xmlPrinter.print(jsonE);
        }


    }
}