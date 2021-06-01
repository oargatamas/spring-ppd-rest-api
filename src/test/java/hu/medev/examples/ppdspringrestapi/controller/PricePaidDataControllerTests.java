package hu.medev.examples.ppdspringrestapi.controller;

import hu.medev.examples.ppdspringrestapi.config.PpdSamples;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingPricePaidData;
import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import hu.medev.examples.ppdspringrestapi.service.PricePaidDataService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static hu.medev.examples.ppdspringrestapi.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(PpdSamples.class)
public class PricePaidDataControllerTests {

    @LocalServerPort
    int port;

    String host = "http://localhost";

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    PricePaidDataService serviceMock;

    @Value("${data.dateFormat}")
    String datePattern;

    @Autowired
    @Qualifier(PpdSamples.SAMPLES_ALL)
    List<PricePaidDataRecord> sampleRecords;

    @Autowired
    @Qualifier(PpdSamples.SAMPLES_DATE_FILTERED)
    List<PricePaidDataRecord> dateFilteredSamples;

    @Test
    public void getPriceDataRecordByIdWithExistingIdExpectSampleRecord() throws Exception {
        PricePaidDataRecord sampleRecord = sampleRecords.get(0);
        String existingId = sampleRecord.getTransactionUniqueIdentifier();
        String url = host + ":" + port + "/ppd/" + existingId;
        Mockito.when(serviceMock.findRecordById(sampleRecord.getTransactionUniqueIdentifier()))
                .thenReturn(sampleRecord);

        ResponseEntity<PricePaidDataRecord> response = restTemplate.getForEntity(url, PricePaidDataRecord.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertContentTypeIsJsonAndHal(response.getHeaders());
        assertPpdRecordsAreEqual(response.getBody(), sampleRecord);

    }

    @Test
    public void getPriceDataRecordByIdWithNonExistingIdExpectNotFound() throws Exception {
        PricePaidDataRecord sampleRecord = sampleRecords.get(0);
        String existingId = "invalidIdString";
        String url = host + ":" + port + "/ppd/" + existingId;
        Mockito.when(serviceMock.findRecordById(sampleRecord.getTransactionUniqueIdentifier()))
                .thenReturn(sampleRecord);

        ResponseEntity<PricePaidDataRecord> response = restTemplate.getForEntity(url, PricePaidDataRecord.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertContentTypeIsJsonAndHal(response.getHeaders());
    }

    @Test
    public void getPriceDataForDatePeriodExpectFilteredListOfSampleRecords() throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime periodFrom = LocalDateTime.parse("2006-07-01T00:00:00", formatter);
        LocalDateTime periodUntil = LocalDateTime.parse("2006-07-30T23:59:00", formatter);

        String url = host + ":" + port + "/ppd/from/" + formatter.format(periodFrom) + "/until/" + formatter.format(periodUntil);
        Mockito.when(serviceMock.findAllRecordsBetween(periodFrom,periodUntil,1))
                .thenReturn(PpdSamples.recordsToPagingData(dateFilteredSamples));

        ResponseEntity<PagingPricePaidData> response = restTemplate.getForEntity(url, PagingPricePaidData.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertContentTypeIsJsonAndHal(response.getHeaders());
        assertThat(response.getBody().getPricePaidData()).isNotNull();
        assertPpdRecordCollectionIsEqual(response.getBody().getPricePaidData(),dateFilteredSamples);
    }

    @Test
    public void getAllPriceDataExpectAllSampleRecords() throws Exception{
        String url = host + ":" + port + "/ppd";
        Mockito.when(serviceMock.findAllRecords(1))
                .thenReturn(PpdSamples.recordsToPagingData(sampleRecords));

        ResponseEntity<PagingPricePaidData> response = restTemplate.getForEntity(url, PagingPricePaidData.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertContentTypeIsJsonAndHal(response.getHeaders());
        assertThat(response.getBody().getPricePaidData()).isNotNull();
        assertPpdRecordCollectionIsEqual(response.getBody().getPricePaidData(),sampleRecords);
    }

    @Test
    public void getPriceDataForInvalidDatePeriodExpectBadRequest() throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime periodFrom = LocalDateTime.parse("2006-07-30T00:00:00", formatter);
        LocalDateTime periodUntil = LocalDateTime.parse("2006-07-01T23:59:00", formatter);

        String url = host + ":" + port + "/ppd/from/" + formatter.format(periodFrom) + "/until/" + formatter.format(periodUntil);

        ResponseEntity<PagingPricePaidData> response = restTemplate.getForEntity(url, PagingPricePaidData.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }




}
