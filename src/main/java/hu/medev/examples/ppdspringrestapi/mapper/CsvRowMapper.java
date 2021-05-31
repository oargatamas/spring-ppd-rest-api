package hu.medev.examples.ppdspringrestapi.mapper;

import hu.medev.examples.ppdspringrestapi.model.pdd.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CsvRowMapper {
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm";


    public static PricePaidDataRecord fromCsvRow(String[] csvRow) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        return PricePaidDataRecordBuilder.aPricePaidDataRecord()
                .withTransactionUniqueIdentifier(csvRow[0].replaceAll("[{}]", " "))
                .withPrice(new BigDecimal(csvRow[1]))
                .withDateOfTransfer(LocalDateTime.parse(csvRow[2],formatter))
                .withPostcode(csvRow[3])
                .withPropertyType(PropertyType.valueOf(csvRow[4]))
                .withOldNew(csvRow[5].equals("Y"))
                .withDuration(Duration.valueOf(csvRow[6]))
                .withPaon(csvRow[7])
                .withSaon(csvRow[8])
                .withStreet(csvRow[9])
                .withLocality(csvRow[10])
                .withCity(csvRow[11])
                .withDistrict(csvRow[12])
                .withCounty(csvRow[13])
                .withPpdCategory(Category.valueOf(csvRow[14]))
                .withRecordStatus(RecordStatus.valueOf(csvRow[15]))
                .build();
    }
}
