package eu.tasgroup.hyperskill.jsonparser.app;

import eu.tasgroup.hyperskill.jsonparser.file.FileManager;
import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.printer.JSONtoXMLPrinter;
import eu.tasgroup.hyperskill.jsonparser.traverse.Traverser;
import eu.tasgroup.hyperskill.jsonparser.converter.JSONConverter2;
import eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor2;

import java.io.IOException;

public class DocumentConverter {

    private FileManager fm;
    private Traverser trv;
    private JSONConverter2 jsonConverter;
    private JSONElementVisitor2 jsonElementVisitor2;
    private JSONtoXMLPrinter printer;


    public DocumentConverter() {

        fm=new FileManager();
        trv=new Traverser();
        jsonConverter=new JSONConverter2();
        printer=new JSONtoXMLPrinter();
        jsonElementVisitor2=new JSONElementVisitor2();
    }

    public void convert() throws IOException {

        String text=fm.load("firstCompleteExample.json");

        if (text.startsWith("{")) {

            JSONElement jsonE=trv.traverse(text);

            XMLElement xmlE=jsonConverter.convert(jsonE);

            jsonElementVisitor2.visit(xmlE);
            //printer.printToXMLFormat(xmlE);
        }
    }
}