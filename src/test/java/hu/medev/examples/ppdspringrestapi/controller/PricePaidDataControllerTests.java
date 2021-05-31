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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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



    private void assertContentTypeIsJsonAndHal(HttpHeaders headers) {
        assertThat(headers).containsKey(HttpHeaders.CONTENT_TYPE);
        assertThat(headers.get(HttpHeaders.CONTENT_TYPE).size()).isEqualTo(1);
        assertThat(headers.get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/hal+json");
    }

    private void assertPpdRecordsAreEqual(PricePaidDataRecord record1, PricePaidDataRecord record2) {
        assertThat(record1).isNotNull();
        assertThat(record2).isNotNull();
        assertThat(record1.getTransactionUniqueIdentifier()).isEqualTo(record2.getTransactionUniqueIdentifier());
        assertThat(record1.getPrice()).isEqualTo(record2.getPrice());
        assertThat(record1.getDateOfTransfer()).isEqualTo(record2.getDateOfTransfer());
        assertThat(record1.getPostcode()).isEqualTo(record2.getPostcode());
        assertThat(record1.getPropertyType()).isEqualTo(record2.getPropertyType());
        assertThat(record1.isOldNew()).isEqualTo(record2.isOldNew());
        assertThat(record1.getDuration()).isEqualTo(record2.getDuration());
        assertThat(record1.getPaon()).isEqualTo(record2.getPaon());
        assertThat(record1.getSaon()).isEqualTo(record2.getSaon());
        assertThat(record1.getStreet()).isEqualTo(record2.getStreet());
        assertThat(record1.getLocality()).isEqualTo(record2.getLocality());
        assertThat(record1.getCity()).isEqualTo(record2.getCity());
        assertThat(record1.getDistrict()).isEqualTo(record2.getDistrict());
        assertThat(record1.getCounty()).isEqualTo(record2.getCounty());
        assertThat(record1.getPpdCategory()).isEqualTo(record2.getPpdCategory());
        assertThat(record1.getRecordStatus()).isEqualTo(record2.getRecordStatus());
    }

    private void assertPpdRecordCollectionIsEqual(List<PricePaidDataRecord> records1, List<PricePaidDataRecord> records2){
        assertThat(records1.size()).isEqualTo(records2.size());
        for(int i= 0; i < records1.size(); i++){
            assertPpdRecordsAreEqual(records1.get(i), records2.get(i));
        }
    }
}
