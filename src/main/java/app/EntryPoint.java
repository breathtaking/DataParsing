package app;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.Transformer;
import helpers.XMLFileCreator;
import parsing.XMLHandler;
import java.io.IOException;
import java.io.FileWriter;
import java.sql.*;

public class EntryPoint {

    public static void main(String[] args) {
        InitPoint initPoint = new InitPoint();
        initPoint.setLoginToMySQL("root");
        initPoint.setPasswordToMySQL("");
        initPoint.setNumberOfDataBaseItems(10000);
        initPoint.setUrlToMySQL("jdbc:mysql://localhost:3306/magnet");

        ResultSet allSelectedFields = null;
        long startTime = System.currentTimeMillis();

        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            System.out.println("The driver for successful connection to DB was't found");
        }

        XMLFileCreator xmlfc1 = new XMLFileCreator("1.xml");
        xmlfc1.createXMLFile();

        try (Connection connection = DriverManager.getConnection(
                initPoint.getUrlToMySQL(), initPoint.getLoginToMySQL(), initPoint.getPasswordToMySQL());
             Statement statement = connection.createStatement();
             FileWriter writer = new FileWriter(xmlfc1.getRelativeFilePath(), false)) {

            ResultSet setOfLinesCount = statement.executeQuery("SELECT COUNT(*) AS COUNT FROM MAGNET.TEST");
            setOfLinesCount.next();
            int numberOfLines = setOfLinesCount.getInt("COUNT");

            if (numberOfLines > 0) {
                statement.execute("DELETE FROM `MAGNET`.`TEST`");
                System.out.println(numberOfLines + " old items were deleted from DB");

                for (int i = 1; i <= initPoint.getNumberOfDataBaseItems(); i++) {
                    statement.execute(String.format("INSERT INTO `MAGNET`.`TEST`(`FIELD`) VALUES (%d)", i));
                }
                System.out.println(initPoint.getNumberOfDataBaseItems() + " new items were added to DB");
                allSelectedFields = statement.executeQuery("SELECT * FROM MAGNET.TEST");
            } else {
                for (int i = 1; i <= initPoint.getNumberOfDataBaseItems(); i++) {
                    statement.execute(String.format("INSERT INTO `MAGNET`.`TEST`(`FIELD`) VALUES (%d)", i));
                    allSelectedFields = statement.executeQuery("SELECT * FROM MAGNET.TEST");
                }
                System.out.println("DB was empty. " + initPoint.getNumberOfDataBaseItems() + " new items were added");
            }

            System.out.println("Writing to " + xmlfc1.getFileName() + " started");
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.write("\n");
            writer.write("<entries>");
            writer.write("\n");
            while (allSelectedFields.next()) {
                writer.write("\t<entry>\n");
                writer.write(String.format("\t\t <field>%d</field>\n", allSelectedFields.getInt(1)));
                writer.write("\t</entry>\n");
            }
            writer.write("</entries>");
            writer.flush();
            System.out.println("Writing to " + xmlfc1.getFileName() + " ended");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        XMLFileCreator xmlfc2 = new XMLFileCreator("2.xml");
        xmlfc2.createXMLFile();

        try {
            System.out.println("XSLT Processing started");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(new StreamSource(
                    "src/main/resources/xml/stylesheet.xsl"));
            transformer.transform(
                    new StreamSource(
                            xmlfc1.getRelativeFilePath()),
                    new StreamResult(
                            xmlfc2.getRelativeFilePath()));
            System.out.println("XSLT Processing ended");
            System.out.println("File " + xmlfc1.getFileName() + " was transformed to " + xmlfc2.getFileName());
        } catch (TransformerException e) {
            e.printStackTrace();
        }


        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            XMLHandler handler = new XMLHandler();
            saxParser.parse(xmlfc2.getRelativeFilePath(), handler);
            System.out.println("Sum of ('FIELD') values = " + handler.getBigInteger());

        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Application have spent " +
                ((endTime - startTime) / 1000) + " seconds while processing the code");
    }
}

