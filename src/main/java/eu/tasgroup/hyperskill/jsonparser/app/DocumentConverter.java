package eu.tasgroup.hyperskill.jsonparser.app;

import eu.tasgroup.hyperskill.jsonparser.file.FileManager;
import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;
import eu.tasgroup.hyperskill.jsonparser.model.XMLElement;
import eu.tasgroup.hyperskill.jsonparser.printer.JSONtoXMLPrinter;
import eu.tasgroup.hyperskill.jsonparser.traverse.Traverser;
import eu.tasgroup.hyperskill.jsonparser.converter.JSONConverter2;
import eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor2;
import eu.tasgroup.hyperskill.jsonparser.visitor.JSONElementVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;

public class DocumentConverter {

    private FileManager fm;
    private Traverser trv;
    private JSONConverter2 jsonConverter;
    private JSONElementVisitor jsonElementVisitor;
    private JSONtoXMLPrinter printer;


    public DocumentConverter() {

        fm=new FileManager();
        trv=new Traverser();
        jsonConverter=new JSONConverter2();
        printer=new JSONtoXMLPrinter();
        jsonElementVisitor=new JSONElementVisitor();
    }

    public void convert() throws IOException {

        String text=fm.load("firstCompleteExample.json");
        String pathCreated = FileSystems.getDefault().getPath("D:/GIT/converter/src/test/resources", "file.txt").toString();
        if (text.startsWith("{")) {

            JSONElement jsonE=trv.traverse(text);

            XMLElement xmlE=jsonConverter.convert(jsonE);

            String s = jsonElementVisitor.createString(xmlE);
            
            File f = fm.writeOnFile(pathCreated, s);
            //printer.printToXMLFormat(xmlE);
        }
    }
}