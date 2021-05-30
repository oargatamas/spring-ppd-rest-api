package hu.medev.examples.ppdspringrestapi.model.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PagingDetails {
    private int pageNumber;
    private long startRecord;
    private long endRecord;
    @JsonIgnore
    private boolean hasNextPage;
    @JsonIgnore
    private boolean hasPrevPage;
}
