package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Locale;

public class AddressApplication {

    private final ConsoleInput input;
    private final StatisticsPrinter printer;
    private final AddressParser csvParser;
    private final AddressParser xmlParser;

    public AddressApplication(
            ConsoleInput input,
            StatisticsPrinter printer,
            AddressParser csvParser,
            AddressParser xmlParser
    ) {
        this.input = input;
        this.printer = printer;
        this.csvParser = csvParser;
        this.xmlParser = xmlParser;
    }

    public void run() {

        System.out.println(
                "Статистика городских адресов (CSV и XML)"
        );

        while (true) {

            String enteredPath = input.readPath();

            if (enteredPath == null
                    || enteredPath.equals("0")
                    || enteredPath.equalsIgnoreCase("exit")) {

                System.out.println("Программа завершена.");
                return;
            }

            if (enteredPath.isBlank()) {
                System.out.println(
                        "Путь к файлу не может быть пустым."
                );
                continue;
            }

            try {

                Path file = Path.of(
                        removeOuterQuotes(enteredPath)
                );

                if (!Files.isRegularFile(file)) {
                    System.out.println(
                            "Файл не найден: " + file
                    );
                    continue;
                }

                AddressParser parser = chooseParser(file);

                if (parser == null) {
                    System.out.println(
                            "Поддерживаются только файлы .csv и .xml."
                    );
                    continue;
                }

                StatisticsService statistics =
                        new StatisticsService();

                long startTime = System.nanoTime();

                parser.parse(file, statistics::add);

                long elapsedNanos =
                        System.nanoTime() - startTime;

                printer.print(statistics, elapsedNanos);

            } catch (IOException
                     | InvalidPathException
                     | SecurityException e) {

                System.out.println(
                        "Ошибка обработки файла: "
                                + e.getMessage()
                );
            }

            System.out.println();
        }
    }

    private AddressParser chooseParser(Path file) {

        String name = file.getFileName()
                .toString()
                .toLowerCase(Locale.ROOT);

        if (name.endsWith(".csv")) {
            return csvParser;
        }

        if (name.endsWith(".xml")) {
            return xmlParser;
        }

        return null;
    }

    private String removeOuterQuotes(String value) {

        if (value.length() >= 2
                && value.startsWith("\"")
                && value.endsWith("\"")) {

            return value.substring(1, value.length() - 1);
        }

        return value;
    }
}
