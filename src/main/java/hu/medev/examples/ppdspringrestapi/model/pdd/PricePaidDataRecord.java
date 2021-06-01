package hu.medev.examples.ppdspringrestapi.model.pdd;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class PricePaidDataRecord  extends RepresentationModel<PricePaidDataRecord> {
    private String transactionUniqueIdentifier;
    private BigDecimal price;
    private LocalDateTime dateOfTransfer;
    private String postcode;
    private PropertyType propertyType;
    private boolean oldNew;
    private Duration duration;
    private String paon;
    private String saon;
    private String street;
    private String locality;
    private String city;
    private String district;
    private String county;
    private Category ppdCategory;
    private RecordStatus recordStatus;
}
