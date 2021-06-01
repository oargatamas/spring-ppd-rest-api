package hu.medev.examples.ppdspringrestapi.service;

import hu.medev.examples.ppdspringrestapi.model.paging.PagingDetails;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingDetailsBuilder;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingPricePaidData;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingPricePaidDataBuilder;
import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import hu.medev.examples.ppdspringrestapi.repository.PricePaidDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PricePaidDataService {

    @Value("${data.ppd.pageSize}")
    private int PAGE_SIZE;

    private final PricePaidDataRepository dataRepository;

    public PricePaidDataService(PricePaidDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public PricePaidDataRecord findRecordById(String transactionId) {
        return dataRepository.findRecordById(transactionId);
    }

    public PagingPricePaidData findAllRecords(int pageNo) {
        PagingDetails paging = initPagingDetails(pageNo);
        List<PricePaidDataRecord> records = dataRepository.findAllRecords(paging.getStartRecord(), PAGE_SIZE);

        return PagingPricePaidDataBuilder.aPagingPricePaidData()
                .withPricePaidData(records)
                .withPagingDetails(updatePagingDetails(paging,records))
                .build();
    }

    public PagingPricePaidData findAllRecordsBetween(LocalDateTime from, LocalDateTime until, int pageNo) {
        PagingDetails paging = initPagingDetails(pageNo);
        List<PricePaidDataRecord> records = dataRepository.findAllRecordsBetween(from, until, paging.getStartRecord(), PAGE_SIZE);

        return PagingPricePaidDataBuilder.aPagingPricePaidData()
                .withPricePaidData(records)
                .withPagingDetails(updatePagingDetails(paging,records))
                .build();
    }

    private PagingDetails initPagingDetails(int pageNo) {
        long start = PAGE_SIZE * (pageNo - 1);

        return PagingDetailsBuilder.aPagingDetails()
                .withStartRecord(start)
                .withEndRecord(start) // by default 0 record contained in paging. Query results can modify this value
                .withPageNumber(pageNo)
                .build();
    }

    private PagingDetails updatePagingDetails(PagingDetails paging, List<PricePaidDataRecord> records){
        paging.setEndRecord(paging.getEndRecord() + records.size());

        if(paging.getPageNumber() > 1){
            paging.setHasPrevPage(true);
        }

        if(records.size() >= PAGE_SIZE){
            paging.setHasNextPage(true);
        }

        return paging;
    }
}
