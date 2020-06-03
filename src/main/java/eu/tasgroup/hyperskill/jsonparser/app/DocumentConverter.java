package eu.tasgroup.hyperskill.jsonparser.app;

import eu.tasgroup.hyperskill.jsonparser.file.FileManager;
import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.printer.JSONtoXMLPrinter;
import eu.tasgroup.hyperskill.jsonparser.traverse.JSONTraverser;
import eu.tasgroup.hyperskill.jsonparser.converter.JSONConverter;
import eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor;

import java.io.File;
import java.io.IOException;

public class DocumentConverter {

    private FileManager fm;
    private JSONTraverser trv;
    private JSONConverter jsonConverter;
    private JSONtoXMLPrinter printer;


    public DocumentConverter() {

        fm=new FileManager();
        trv=new JSONTraverser();
        jsonConverter=new JSONConverter();
        printer=new JSONtoXMLPrinter();
    }

    public void convertJsonToXml() throws IOException {

        String text=fm.load("firstCompleteExample.json");
        if (text.startsWith("{")) {

            JSONElement jsonE=trv.traverse(text);

            XMLElement xmlE=jsonConverter.convert(jsonE);

            String jsonToXml = printer.printToXMLFormat(xmlE);
            File file = fm.writeOnFile("D:\\GIT\\converter\\src\\test\\resources\\file.txt", jsonToXml);
            //File f = fm.writeOnFile("C:\\Users\\donatom\\IdeaProjects\\BertucciolisConverter\\src\\main\\java\\eu\\tasgroup\\hyperskill\\jsonparser\\app\\text.txt", s);
        }
    }
}