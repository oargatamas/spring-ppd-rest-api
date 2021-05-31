package hu.medev.examples.ppdspringrestapi.config;

import com.opencsv.CSVReader;
import hu.medev.examples.ppdspringrestapi.mapper.CsvRowMapper;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingDetails;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingDetailsBuilder;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingPricePaidData;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingPricePaidDataBuilder;
import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


@TestConfiguration
public class PpdSamples {
    public static final String SAMPLES_ALL = "samples_all";
    public static final String SAMPLES_DATE_FILTERED = "samples_date_filtered";

    @Bean(name = SAMPLES_ALL)
    public List<PricePaidDataRecord> sampleRecords() throws Exception {
        return readCsvSamples("samples_all.csv");
    }

    @Bean(name = SAMPLES_DATE_FILTERED)
    public List<PricePaidDataRecord> sampleRecordsFilteredByDate() throws Exception {
        return readCsvSamples("samples_date_filtered.csv");
    }

    public static List<PricePaidDataRecord> readCsvSamples(String fileName) throws Exception {
        Path path = Paths.get(new ClassPathResource(fileName).getURI());
        Reader reader = Files.newBufferedReader(path);
        CSVReader csv = new CSVReader(reader);

        return csv.readAll().stream()
                .map(CsvRowMapper::fromCsvRow)
                .collect(Collectors.toList());
    }

    public static PagingPricePaidData recordsToPagingData(List<PricePaidDataRecord> records){
        return PagingPricePaidDataBuilder.aPagingPricePaidData()
                .withPagingDetails(PagingDetailsBuilder.aPagingDetails()
                        .withStartRecord(1)
                        .withEndRecord(records.size())
                        .withPageNumber(1)
                        .build())
                .withPricePaidData(records)
                .build();

    }

    public static PagingPricePaidData recordsToPagingData(List<PricePaidDataRecord> records, PagingDetails paging){
        return PagingPricePaidDataBuilder.aPagingPricePaidData()
                .withPagingDetails(paging)
                .withPricePaidData(records)
                .build();

    }
}
