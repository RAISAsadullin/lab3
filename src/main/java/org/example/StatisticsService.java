package org.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class StatisticsService {

    private final Map<AddressRecord, Integer> occurrences =
            new HashMap<>();

    private final Map<String, int[]> floorsByCity =
            new TreeMap<>();

    private int totalRecords;

    public void add(AddressRecord record) {

        totalRecords++;

        occurrences.merge(record, 1, Integer::sum);

        if (record.getFloor() >= 1 && record.getFloor() <= 5) {

            int[] counts = floorsByCity.computeIfAbsent(
                    record.getCity(),
                    city -> new int[5]
            );

            counts[record.getFloor() - 1]++;
        }
    }

    public Map<AddressRecord, Integer> getOccurrences() {
        return Collections.unmodifiableMap(occurrences);
    }

    public Map<String, int[]> getFloorsByCity() {
        return Collections.unmodifiableMap(floorsByCity);
    }

    public int getTotalRecords() {
        return totalRecords;
    }
}
