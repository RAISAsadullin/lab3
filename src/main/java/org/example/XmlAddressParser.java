package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlAddressParser implements AddressParser {

    @Override
    public void parse(
            Path file,
            Consumer<AddressRecord> consumer
    ) throws IOException {

        XMLInputFactory factory = XMLInputFactory.newFactory();

        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(
                XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false
        );

        try (InputStream input = Files.newInputStream(file)) {

            XMLStreamReader reader =
                    factory.createXMLStreamReader(input);

            try {
                while (reader.hasNext()) {

                    if (reader.next() == XMLStreamConstants.START_ELEMENT
                            && "item".equals(reader.getLocalName())) {

                        String city = attribute(reader, "city");
                        String street = attribute(reader, "street");
                        String house = attribute(reader, "house");
                        String floor = attribute(reader, "floor");

                        try {
                            consumer.accept(new AddressRecord(
                                    city,
                                    street,
                                    house,
                                    Integer.parseInt(floor)
                            ));

                        } catch (IllegalArgumentException e) {

                            throw new IOException(
                                    "Ошибка XML в строке "
                                            + reader.getLocation().getLineNumber()
                                            + ": " + e.getMessage(), e
                            );
                        }
                    }
                }

            } finally {
                reader.close();
            }

        } catch (XMLStreamException e) {
            throw new IOException(
                    "Не удалось прочитать XML: " + e.getMessage(), e
            );
        }
    }

    private String attribute(
            XMLStreamReader reader,
            String name
    ) throws IOException {

        String value = reader.getAttributeValue(null, name);

        if (value == null) {
            throw new IOException(
                    "В XML отсутствует атрибут '" + name
                            + "' в строке "
                            + reader.getLocation().getLineNumber()
            );
        }

        return value;
    }
}
