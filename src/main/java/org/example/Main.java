package org.example;

public class Main {

    public static void main(String[] args) {

        AddressApplication application = new AddressApplication(
                new ConsoleInput(),
                new StatisticsPrinter(),
                new CsvAddressParser(),
                new XmlAddressParser()
        );

        application.run();
    }
}
