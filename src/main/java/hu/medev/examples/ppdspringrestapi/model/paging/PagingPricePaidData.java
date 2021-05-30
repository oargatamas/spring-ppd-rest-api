package hu.medev.examples.ppdspringrestapi.model.paging;


import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class PagingPricePaidData  extends RepresentationModel<PagingPricePaidData> {
    private PagingDetails pagingDetails;
    private List<PricePaidDataRecord> pricePaidData;
}
