package helpers;

import java.io.File;
import java.io.IOException;

public class XMLFileCreator {
    private String fileSeparator = System.getProperty("file.separator");
    private String relativeXMLFilesFolderPath = "src" + fileSeparator + "main" + fileSeparator +
            "resources" + fileSeparator + "xml" + fileSeparator;
    private String fileName;
    private String relativeFilePath;

    public XMLFileCreator(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRelativeFilePath() {
        return relativeXMLFilesFolderPath + fileName;
    }

    public void createXMLFile() {
        File xmlFile = new File(relativeXMLFilesFolderPath + fileName);
        try {
            if(xmlFile.createNewFile()){
                System.out.println(fileName + " file was successfully created in \"xml\" directory");
            }else
                System.out.println("File " + fileName + " is already exists in \"xml\" directory");
        }
        catch (IOException e) {
            e.getMessage();
            System.out.println("There were issues while creating a new File");
        }
    }
}


//https://softwarecave.org/2014/02/15/write-xml-documents-using-streaming-api-for-xml-stax/