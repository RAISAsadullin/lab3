package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class CsvAddressParser implements AddressParser {

    @Override
    public void parse(
            Path file,
            Consumer<AddressRecord> consumer
    ) throws IOException {

        try (BufferedReader reader =
                     Files.newBufferedReader(file, StandardCharsets.UTF_8)) {

            String header = reader.readLine();

            if (header == null) {
                throw new IOException("CSV-файл пуст.");
            }

            header = header.replace("\uFEFF", "");

            List<String> columns;

            try {
                columns = splitLine(header);
            } catch (IllegalArgumentException e) {
                throw new IOException(
                        "Некорректный заголовок CSV: " + e.getMessage(), e
                );
            }

            Map<String, Integer> indexes = new HashMap<>();

            for (int i = 0; i < columns.size(); i++) {
                indexes.put(
                        columns.get(i).trim().toLowerCase(Locale.ROOT), i
                );
            }

            for (String field : List.of("city", "street", "house", "floor")) {
                if (!indexes.containsKey(field)) {
                    throw new IOException(
                            "В CSV отсутствует столбец: " + field
                    );
                }
            }

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                try {
                    List<String> values = splitLine(line);

                    AddressRecord record = new AddressRecord(
                            field(values, indexes, "city"),
                            field(values, indexes, "street"),
                            field(values, indexes, "house"),
                            Integer.parseInt(
                                    field(values, indexes, "floor")
                            )
                    );

                    consumer.accept(record);

                } catch (IllegalArgumentException
                         | IndexOutOfBoundsException e) {

                    throw new IOException(
                            "Ошибка CSV в строке "
                                    + lineNumber + ": " + e.getMessage(), e
                    );
                }
            }
        }
    }

    private String field(
            List<String> values,
            Map<String, Integer> indexes,
            String name
    ) {
        return values.get(indexes.get(name)).trim();
    }

    private List<String> splitLine(String line) {

        List<String> values = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {

            char current = line.charAt(i);

            if (current == '"') {
                if (quoted && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    value.append('"');
                    i++;

                } else {
                    quoted = !quoted;
                }

            } else if (current == ';' && !quoted) {

                values.add(value.toString().trim());
                value.setLength(0);

            } else {
                value.append(current);
            }
        }

        if (quoted) {
            throw new IllegalArgumentException(
                    "Не закрыты кавычки."
            );
        }

        values.add(value.toString().trim());
        return values;
    }
}
