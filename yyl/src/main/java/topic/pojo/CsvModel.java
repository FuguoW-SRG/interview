package topic.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

/**
 * @ClassName CsvModel
 * @Author yyl
 * @Date 2024-02-05 11:40:01
 * @Description CsvModel
 * @Version 1.0
 */
@CsvRecord(separator = ",", skipFirstLine = true, generateHeaderColumns = true)
public class CsvModel {

    @DataField(pos = 1)
    private String orderIdentifier;

    @DataField(pos = 2)
    private String orderType;

    @JsonProperty(value = "vendor.organizationIdentifier")
    @DataField(pos = 3)
    private String vendor;

    @JsonProperty(value = "buyer.organizationIdentifier")
    @DataField(pos = 4)
    private String buyer;

    @JsonProperty(value = "shipFromInstructionLocation.locationIdentifier")
    @DataField(pos = 5)
    private String shipFromInstructionLocation;

    @JsonProperty(value = "shipToLocation.locationIdentifier")
    @DataField(pos = 6)
    private String shipToLocation;

    @DataField(pos = 7)
    private String orderStatus;

    @DataField(pos = 8)
    private String createdDate;

    @DataField(pos = 9)
    private String requestedShipDate;

    @DataField(pos = 10)
    private String requestedDeliveryDate;

    @DataField(pos = 11)
    private String plannedShipDate;

    @DataField(pos = 12)
    private String plannedDeliveryDate;

    @DataField(pos = 13)
    private String quantity;

    @DataField(pos = 14)
    private String quantityUnits;

    @DataField(pos = 15)
    private String totalValue;

    @DataField(pos = 16)
    private String orderValueCurrency;

    @DataField(pos = 17)
    private String lineCount;

    @DataField(pos = 18)
    private String totalShippedQuantity;

    @DataField(pos = 19)
    private String exclude;

    @DataField(pos = 20)
    private String sourceLink;

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getShipFromInstructionLocation() {
        return shipFromInstructionLocation;
    }

    public void setShipFromInstructionLocation(String shipFromInstructionLocation) {
        this.shipFromInstructionLocation = shipFromInstructionLocation;
    }

    public String getShipToLocation() {
        return shipToLocation;
    }

    public void setShipToLocation(String shipToLocation) {
        this.shipToLocation = shipToLocation;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getRequestedShipDate() {
        return requestedShipDate;
    }

    public void setRequestedShipDate(String requestedShipDate) {
        this.requestedShipDate = requestedShipDate;
    }

    public String getRequestedDeliveryDate() {
        return requestedDeliveryDate;
    }

    public void setRequestedDeliveryDate(String requestedDeliveryDate) {
        this.requestedDeliveryDate = requestedDeliveryDate;
    }

    public String getPlannedShipDate() {
        return plannedShipDate;
    }

    public void setPlannedShipDate(String plannedShipDate) {
        this.plannedShipDate = plannedShipDate;
    }

    public String getPlannedDeliveryDate() {
        return plannedDeliveryDate;
    }

    public void setPlannedDeliveryDate(String plannedDeliveryDate) {
        this.plannedDeliveryDate = plannedDeliveryDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnits() {
        return quantityUnits;
    }

    public void setQuantityUnits(String quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getOrderValueCurrency() {
        return orderValueCurrency;
    }

    public void setOrderValueCurrency(String orderValueCurrency) {
        this.orderValueCurrency = orderValueCurrency;
    }

    public String getLineCount() {
        return lineCount;
    }

    public void setLineCount(String lineCount) {
        this.lineCount = lineCount;
    }

    public String getTotalShippedQuantity() {
        return totalShippedQuantity;
    }

    public void setTotalShippedQuantity(String totalShippedQuantity) {
        this.totalShippedQuantity = totalShippedQuantity;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }
}
