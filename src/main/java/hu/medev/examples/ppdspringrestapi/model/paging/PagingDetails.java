package hu.medev.examples.ppdspringrestapi.model.paging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
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
