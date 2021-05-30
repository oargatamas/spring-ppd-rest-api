package hu.medev.examples.ppdspringrestapi.model.paging;

public final class PagingDetailsBuilder {
    private int pageNumber;
    private long startRecord;
    private long endRecord;

    private PagingDetailsBuilder() {
    }

    public static PagingDetailsBuilder aPagingDetails() {
        return new PagingDetailsBuilder();
    }

    public PagingDetailsBuilder withPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public PagingDetailsBuilder withStartRecord(long startRecord) {
        this.startRecord = startRecord;
        return this;
    }

    public PagingDetailsBuilder withEndRecord(long endRecord) {
        this.endRecord = endRecord;
        return this;
    }


    public PagingDetails build() {
        PagingDetails pagingDetails = new PagingDetails();
        pagingDetails.setPageNumber(pageNumber);
        pagingDetails.setStartRecord(startRecord);
        pagingDetails.setEndRecord(endRecord);
        return pagingDetails;
    }
}
