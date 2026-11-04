package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface AddressParser {

    void parse(
            Path file,
            Consumer<AddressRecord> consumer
    ) throws IOException;
}
