package hu.medev.examples.ppdspringrestapi.repository;

import hu.medev.examples.ppdspringrestapi.config.PpdSamples;
import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import hu.medev.examples.ppdspringrestapi.repository.csv.CsvPricePaidDataRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static hu.medev.examples.ppdspringrestapi.TestUtils.assertPpdRecordCollectionIsEqual;
import static hu.medev.examples.ppdspringrestapi.TestUtils.assertPpdRecordsAreEqual;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(PpdSamples.class)
@SpringBootTest
public class CsvPricePaidDataRepositoryTests {

    CsvPricePaidDataRepository csvRepository = new CsvPricePaidDataRepository() {
        @SneakyThrows
        @Override
        protected Path fetchCSVData() {
            switch (fetchType){
                case PpdSamples.SAMPLES_DATE_FILTERED:
                    return Paths.get(new ClassPathResource("samples/samples_date_filtered.csv").getURI());
                default:
                    return Paths.get(new ClassPathResource("samples/samples_all.csv").getURI());
            }
        }
    };

    private static String fetchType;

    @Value("${data.ppd.pageSize}")
    private int PAGE_SIZE;

    @Value("${data.dateFormat}")
    String datePattern;

    @Autowired
    @Qualifier(PpdSamples.SAMPLES_ALL)
    List<PricePaidDataRecord> sampleRecords;

    @Autowired
    @Qualifier(PpdSamples.SAMPLES_DATE_FILTERED)
    List<PricePaidDataRecord> dateFilteredSamples;

    @Test
    public void getPriceDataRecordByIdWithExistingIdExpectSampleRecord() {
        fetchType = PpdSamples.SAMPLES_ALL;
        PricePaidDataRecord sampleRecord = sampleRecords.get(0);

        PricePaidDataRecord response = csvRepository.findRecordById(sampleRecord.getTransactionUniqueIdentifier());

        assertThat(response).isNotNull();
        assertPpdRecordsAreEqual(response, sampleRecord);
    }

    @Test
    public void getPriceDataRecordByIdWithNonExistingIdExpectNull() {
        String sampleRecordId = "InvalidIdString123asdf";

        PricePaidDataRecord response = csvRepository.findRecordById(sampleRecordId);

        assertThat(response).isNull();
    }

    @Test
    public void getPriceDataForDatePeriodExpectFilteredListOfSampleRecords() throws Exception {
        fetchType = PpdSamples.SAMPLES_DATE_FILTERED;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime periodFrom = LocalDateTime.parse("2006-07-01T00:00:00", formatter);
        LocalDateTime periodUntil = LocalDateTime.parse("2006-07-30T23:59:00", formatter);


        List<PricePaidDataRecord> response = csvRepository.findAllRecordsBetween(periodFrom, periodUntil, 0, PAGE_SIZE);

        assertThat(response).isNotNull();
        assertPpdRecordCollectionIsEqual(response, dateFilteredSamples);
    }

    @Test
    public void getPriceDataForInvalidDatePeriodExpectEmptyList() throws Exception {
        fetchType = PpdSamples.SAMPLES_DATE_FILTERED;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime periodFrom = LocalDateTime.parse("2006-07-30T00:00:00", formatter);
        LocalDateTime periodUntil = LocalDateTime.parse("2006-07-01T23:59:00", formatter);


        List<PricePaidDataRecord> response = csvRepository.findAllRecordsBetween(periodFrom, periodUntil, 0, PAGE_SIZE);

        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

    @Test
    public void getAllPriceDataExpectAllSampleRecords() throws Exception {
        fetchType = PpdSamples.SAMPLES_ALL;
        List<PricePaidDataRecord> response = csvRepository.findAllRecords(0,PAGE_SIZE);

        assertThat(response).isNotNull();
        assertPpdRecordCollectionIsEqual(response, sampleRecords);
    }
}
