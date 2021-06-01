package hu.medev.examples.ppdspringrestapi.repository.csv;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemCachedCsvPricePaidDataRepository extends CsvPricePaidDataRepository{

    @Value("${data.ppd.localUri}")
    private String CSV_PATH;

    @SneakyThrows
    @Override
    protected Path fetchCSVData() {
        return Paths.get(URI.create(CSV_PATH));
    }
}
