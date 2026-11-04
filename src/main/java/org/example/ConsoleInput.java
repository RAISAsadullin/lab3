package org.example;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ConsoleInput {

    private final Scanner scanner =
            new Scanner(System.in, StandardCharsets.UTF_8);

    public String readPath() {

        System.out.print(
                "Путь к CSV/XML-файлу (0 — выход): "
        );

        if (!scanner.hasNextLine()) {
            return null;
        }

        return scanner.nextLine().trim();
    }
}
