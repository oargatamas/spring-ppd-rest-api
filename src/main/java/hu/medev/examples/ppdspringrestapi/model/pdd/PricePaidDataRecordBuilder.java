package hu.medev.examples.ppdspringrestapi.model.pdd;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class PricePaidDataRecordBuilder {
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

    private PricePaidDataRecordBuilder() {
    }

    public static PricePaidDataRecordBuilder aPricePaidDataRecord() {
        return new PricePaidDataRecordBuilder();
    }

    public PricePaidDataRecordBuilder withTransactionUniqueIdentifier(String transactionUniqueIdentifier) {
        this.transactionUniqueIdentifier = transactionUniqueIdentifier;
        return this;
    }

    public PricePaidDataRecordBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public PricePaidDataRecordBuilder withDateOfTransfer(LocalDateTime dateOfTransfer) {
        this.dateOfTransfer = dateOfTransfer;
        return this;
    }

    public PricePaidDataRecordBuilder withPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public PricePaidDataRecordBuilder withPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
        return this;
    }

    public PricePaidDataRecordBuilder withOldNew(boolean oldNew) {
        this.oldNew = oldNew;
        return this;
    }

    public PricePaidDataRecordBuilder withDuration(Duration duration) {
        this.duration = duration;
        return this;
    }

    public PricePaidDataRecordBuilder withPaon(String paon) {
        this.paon = paon;
        return this;
    }

    public PricePaidDataRecordBuilder withSaon(String saon) {
        this.saon = saon;
        return this;
    }

    public PricePaidDataRecordBuilder withStreet(String street) {
        this.street = street;
        return this;
    }

    public PricePaidDataRecordBuilder withLocality(String locality) {
        this.locality = locality;
        return this;
    }

    public PricePaidDataRecordBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public PricePaidDataRecordBuilder withDistrict(String district) {
        this.district = district;
        return this;
    }

    public PricePaidDataRecordBuilder withCounty(String county) {
        this.county = county;
        return this;
    }

    public PricePaidDataRecordBuilder withPpdCategory(Category ppdCategory) {
        this.ppdCategory = ppdCategory;
        return this;
    }

    public PricePaidDataRecordBuilder withRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
        return this;
    }

    public PricePaidDataRecord build() {
        PricePaidDataRecord pricePaidDataRecord = new PricePaidDataRecord();
        pricePaidDataRecord.setTransactionUniqueIdentifier(transactionUniqueIdentifier);
        pricePaidDataRecord.setPrice(price);
        pricePaidDataRecord.setDateOfTransfer(dateOfTransfer);
        pricePaidDataRecord.setPostcode(postcode);
        pricePaidDataRecord.setPropertyType(propertyType);
        pricePaidDataRecord.setOldNew(oldNew);
        pricePaidDataRecord.setDuration(duration);
        pricePaidDataRecord.setPaon(paon);
        pricePaidDataRecord.setSaon(saon);
        pricePaidDataRecord.setStreet(street);
        pricePaidDataRecord.setLocality(locality);
        pricePaidDataRecord.setCity(city);
        pricePaidDataRecord.setDistrict(district);
        pricePaidDataRecord.setCounty(county);
        pricePaidDataRecord.setPpdCategory(ppdCategory);
        pricePaidDataRecord.setRecordStatus(recordStatus);
        return pricePaidDataRecord;
    }
}
