package hu.medev.examples.ppdspringrestapi.service;

import hu.medev.examples.ppdspringrestapi.config.PpdSamples;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingPricePaidData;
import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import hu.medev.examples.ppdspringrestapi.repository.PricePaidDataRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static hu.medev.examples.ppdspringrestapi.TestUtils.assertPpdRecordCollectionIsEqual;
import static hu.medev.examples.ppdspringrestapi.TestUtils.assertPpdRecordsAreEqual;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Import(PpdSamples.class)
public class PricePaidDataServiceTests {

    @Autowired
    PricePaidDataService service;

    @Value("${data.ppd.pageSize}")
    private int PAGE_SIZE;

    @MockBean
    PricePaidDataRepository repositoryMock;

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
        PricePaidDataRecord sampleRecord = sampleRecords.get(0);

        Mockito.when(repositoryMock.findRecordById(sampleRecord.getTransactionUniqueIdentifier()))
                .thenReturn(sampleRecord);

        PricePaidDataRecord response = service.findRecordById(sampleRecord.getTransactionUniqueIdentifier());

        assertThat(response).isNotNull();
        assertPpdRecordsAreEqual(response, sampleRecord);
    }

    @Test
    public void getPriceDataRecordByIdWithNonExistingIdExpectNull() {
        String sampleRecordId = "InvalidIdString123asdf";

        PricePaidDataRecord response = service.findRecordById(sampleRecordId);

        assertThat(response).isNull();
    }

    @Test
    public void getPriceDataForDatePeriodExpectFilteredListOfSampleRecords() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime periodFrom = LocalDateTime.parse("2006-07-01T00:00:00", formatter);
        LocalDateTime periodUntil = LocalDateTime.parse("2006-07-30T23:59:00", formatter);

        Mockito.when(repositoryMock.findAllRecordsBetween(periodFrom, periodUntil, 0, PAGE_SIZE))
                .thenReturn(dateFilteredSamples);

        PagingPricePaidData response = service.findAllRecordsBetween(periodFrom, periodUntil, 1);

        assertThat(response).isNotNull();
        assertThat(response.getPricePaidData()).isNotNull();
        assertThat(response.getPagingDetails()).isNotNull();
        assertPpdRecordCollectionIsEqual(response.getPricePaidData(), dateFilteredSamples);
    }

    @Test
    public void getPriceDataForInvalidDatePeriodExpectEmptyList() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime periodFrom = LocalDateTime.parse("2006-07-30T00:00:00", formatter);
        LocalDateTime periodUntil = LocalDateTime.parse("2006-07-01T23:59:00", formatter);

        Mockito.when(repositoryMock.findAllRecordsBetween(periodFrom, periodUntil, 0, PAGE_SIZE))
                .thenReturn(Collections.emptyList());

        PagingPricePaidData response = service.findAllRecordsBetween(periodFrom, periodUntil, 1);

        assertThat(response).isNotNull();
        assertThat(response.getPricePaidData()).isNotNull();
        assertThat(response.getPagingDetails()).isNotNull();
        assertThat(response.getPricePaidData()).isEmpty();
    }

    @Test
    public void getAllPriceDataExpectAllSampleRecords() throws Exception {
        Mockito.when(repositoryMock.findAllRecords(0, PAGE_SIZE))
                .thenReturn(sampleRecords);

        PagingPricePaidData response = service.findAllRecords(1);


        assertThat(response).isNotNull();
        assertThat(response.getPricePaidData()).isNotNull();
        assertThat(response.getPagingDetails()).isNotNull();
        assertPpdRecordCollectionIsEqual(response.getPricePaidData(), sampleRecords);
    }

}
