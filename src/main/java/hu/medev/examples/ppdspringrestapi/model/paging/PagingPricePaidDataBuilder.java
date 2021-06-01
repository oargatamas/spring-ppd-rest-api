package hu.medev.examples.ppdspringrestapi.model.paging;

import hu.medev.examples.ppdspringrestapi.model.pdd.PricePaidDataRecord;

import java.util.List;

public final class PagingPricePaidDataBuilder {
    private PagingDetails pagingDetails;
    private List<PricePaidDataRecord> pricePaidData;

    private PagingPricePaidDataBuilder() {
    }

    public static PagingPricePaidDataBuilder aPagingPricePaidData() {
        return new PagingPricePaidDataBuilder();
    }

    public PagingPricePaidDataBuilder withPagingDetails(PagingDetails pagingDetails) {
        this.pagingDetails = pagingDetails;
        return this;
    }

    public PagingPricePaidDataBuilder withPricePaidData(List<PricePaidDataRecord> pricePaidData) {
        this.pricePaidData = pricePaidData;
        return this;
    }

    public PagingPricePaidData build() {
        PagingPricePaidData pagingPricePaidData = new PagingPricePaidData();
        pagingPricePaidData.setPagingDetails(pagingDetails);
        pagingPricePaidData.setPricePaidData(pricePaidData);
        return pagingPricePaidData;
    }
}
