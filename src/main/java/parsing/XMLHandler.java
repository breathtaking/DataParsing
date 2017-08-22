package parsing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.math.BigInteger;

public class XMLHandler extends DefaultHandler {
    private BigInteger bigInteger = BigInteger.valueOf(0);
    private boolean field = false;


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("entry")) {
            String fieldValue = attributes.getValue("field");
            bigInteger = bigInteger.add(new BigInteger(fieldValue));
            //System.out.println(bigInteger.toString());
            field = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (field) {
            field = false;
        }
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }
}
