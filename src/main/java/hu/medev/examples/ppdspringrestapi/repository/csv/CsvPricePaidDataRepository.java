package hu.medev.examples.ppdspringrestapi.repository.csv;

import com.opencsv.CSVReader;
import hu.medev.examples.ppdspringrestapi.mapper.CsvRowMapper;
import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import hu.medev.examples.ppdspringrestapi.repository.PricePaidDataRepository;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class CsvPricePaidDataRepository implements PricePaidDataRepository {

    protected final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(CsvRowMapper.DATE_FORMAT);

    @Override
    public PricePaidDataRecord findRecordById(String transactionId) {
        List<PricePaidDataRecord> result = getRecords(line -> line[0].equals("{" + transactionId + "}"),0,1);

        return result.size() > 0 ? result.get(0) : null;
    }

    @Override
    public List<PricePaidDataRecord> findAllRecords(long offset, long numberOfRecords) {
        return getRecords(line -> true, offset, numberOfRecords);
    }


    @Override
    public List<PricePaidDataRecord> findAllRecordsBetween(LocalDateTime from, LocalDateTime until, long offset, long numberOfRecords) {
        return getRecords(line -> {
            LocalDateTime lineDate = LocalDateTime.parse(line[2], dateFormat);
            return lineDate.isAfter(from) && lineDate.isBefore(until);
        }, offset, numberOfRecords);
    }

    @SneakyThrows
    protected List<PricePaidDataRecord> getRecords(Predicate<String[]> filter, long offset, long numberOfRecords) {
        List<PricePaidDataRecord> records = new ArrayList<>();

        BufferedReader reader = Files.newBufferedReader(fetchCSVData());

        CSVReader csvReader = new CSVReader(reader);
        String[] line;
        long i = 0;
        long matches = 0;
        while ((matches < numberOfRecords) && (line = csvReader.readNext()) != null) {
            if (i >= offset && filter.test(line)) {
                records.add(CsvRowMapper.fromCsvRow(line));
                matches++;
            }
            i++;
        }
        reader.close();
        csvReader.close();

        cleanUp();

        return records;
    }

    protected abstract Path fetchCSVData();

    protected void cleanUp(){
        //DO NOTHING. DESCENDANTS CAN CALL THIS METHOD TO CLEAN UP THEIR RESOURCES.
    }


}
