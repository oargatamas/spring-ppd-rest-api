package hu.medev.examples.ppdspringrestapi;

import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

    public static void assertContentTypeIsJsonAndHal(HttpHeaders headers) {
        assertThat(headers).containsKey(HttpHeaders.CONTENT_TYPE);
        assertThat(headers.get(HttpHeaders.CONTENT_TYPE).size()).isEqualTo(1);
        assertThat(headers.get(HttpHeaders.CONTENT_TYPE).get(0)).isEqualTo("application/hal+json");
    }

    public static void assertPpdRecordsAreEqual(PricePaidDataRecord record1, PricePaidDataRecord record2) {
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

    public static void assertPpdRecordCollectionIsEqual(List<PricePaidDataRecord> records1, List<PricePaidDataRecord> records2){
        assertThat(records1.size()).isEqualTo(records2.size());
        for(int i= 0; i < records1.size(); i++){
            assertPpdRecordsAreEqual(records1.get(i), records2.get(i));
        }
    }
}
