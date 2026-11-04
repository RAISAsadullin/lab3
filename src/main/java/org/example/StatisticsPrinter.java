
package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StatisticsPrinter {

    public void print(
            StatisticsService statistics,
            long elapsedNanos
    ) {

        System.out.println(
                "Обработано записей: " + statistics.getTotalRecords()
        );

        System.out.println("Дублирующиеся записи:");

        List<Map.Entry<AddressRecord, Integer>> duplicates =
                new ArrayList<>();

        for (Map.Entry<AddressRecord, Integer> entry
                : statistics.getOccurrences().entrySet()) {

            if (entry.getValue() > 1) {
                duplicates.add(entry);
            }
        }

        duplicates.sort(
                Comparator.comparing(
                        entry -> entry.getKey().toString()
                )
        );

        if (duplicates.isEmpty()) {

            System.out.println("Дубликатов нет.");

        } else {

            for (Map.Entry<AddressRecord, Integer> entry : duplicates) {

                System.out.println(
                        entry.getKey()
                                + " — повторений: "
                                + entry.getValue()
                );
            }
        }

        System.out.println("Количество зданий по городам:");

        for (Map.Entry<String, int[]> entry
                : statistics.getFloorsByCity().entrySet()) {

            int[] counts = entry.getValue();

            System.out.println(
                    entry.getKey()
                            + ": 1 этаж — " + counts[0]
                            + ", 2 этажа — " + counts[1]
                            + ", 3 этажа — " + counts[2]
                            + ", 4 этажа — " + counts[3]
                            + ", 5 этажей — " + counts[4]
            );
        }

        System.out.printf(
                "Время обработки: %.3f мс%n",
                elapsedNanos / 1_000_000.0
        );
    }
}
