package hu.medev.examples.ppdspringrestapi.controller;

import hu.medev.examples.ppdspringrestapi.model.paging.PagingDetails;
import hu.medev.examples.ppdspringrestapi.model.paging.PagingPricePaidData;
import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import hu.medev.examples.ppdspringrestapi.service.PricePaidDataService;
import io.swagger.annotations.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Api(tags = "PricePaidData API")
@RestController()
@RequestMapping(path = "/ppd")
public class PricePaidDataController {

    private final PricePaidDataService pricePaidDataService;

    public PricePaidDataController(PricePaidDataService pricePaidDataService) {
        this.pricePaidDataService = pricePaidDataService;
    }



    @RequestMapping(path = "/{transactionId}", method = RequestMethod.GET, produces = "application/hal+json")
    public PricePaidDataRecord getPriceDataRecordById(@PathVariable String transactionId){
        PricePaidDataRecord result = pricePaidDataService.findRecordById(transactionId);

        if(result == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"PDD record can not be found");
        }

        result.add(createPpdSelfRelation(result));

        return result;
    }

    @RequestMapping(path = "", method = RequestMethod.GET, produces = "application/hal+json")
    public PagingPricePaidData getAllPricePaidData(@RequestParam Optional<Integer> pageNo){
        PagingPricePaidData result = pricePaidDataService.findAllRecords(pageNo.orElseGet(()->1));

        return createPpdCollectionRelations(result);
    }

    @RequestMapping(path = "/from/{from}/until/{until}", method = RequestMethod.GET, produces = "application/hal+json")
    public PagingPricePaidData getAllPricePaidDataWithinDateRange(
            @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
            @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") LocalDateTime until,
            @RequestParam Optional<Integer> pageNo
    ){

        if(from.compareTo(until) >= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date range provided");
        }

        PagingPricePaidData result = pricePaidDataService.findAllRecordsBetween(from,until,pageNo.orElseGet(()->1));

        return createPpdCollectionRelations(result);
    }

    private Link createPpdSelfRelation(PricePaidDataRecord record){
        return linkTo(methodOn(PricePaidDataController.class)
                .getPriceDataRecordById(record.getTransactionUniqueIdentifier()))
                .withSelfRel();
    }

    private PagingPricePaidData createPpdCollectionRelations(PagingPricePaidData pagingData){
        PagingDetails paging = pagingData.getPagingDetails();

        if(paging.isHasPrevPage()) {
            paging.add(linkTo(methodOn(PricePaidDataController.class)
                    .getAllPricePaidData(Optional.of(paging.getPageNumber() - 1)))
                    .withRel("prevPage"));
        }

        if(paging.isHasNextPage()) {
            paging.add(linkTo(methodOn(PricePaidDataController.class)
                    .getAllPricePaidData(Optional.of(paging.getPageNumber() + 1)))
                    .withRel("nextPage"));
        }

        pagingData.getPricePaidData().forEach(record -> record.add(createPpdSelfRelation(record)));

        return pagingData;
    }
}
