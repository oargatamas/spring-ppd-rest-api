package hu.medev.examples.ppdspringrestapi.model.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class PagingDetails extends RepresentationModel<PagingDetails> {
    private int pageNumber;
    private long startRecord;
    private long endRecord;
    @JsonIgnore
    private boolean hasNextPage;
    @JsonIgnore
    private boolean hasPrevPage;
}
