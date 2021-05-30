package hu.medev.examples.ppdspringrestapi.repository;

import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PricePaidDataRepository {

    PricePaidDataRecord findRecordById(String transactionId);

    List<PricePaidDataRecord> findAllRecords(long offset, long numberOfRecords);

    List<PricePaidDataRecord> findAllRecordsBetween(LocalDateTime from, LocalDateTime until, long offset, long numberOfRecords);

}
