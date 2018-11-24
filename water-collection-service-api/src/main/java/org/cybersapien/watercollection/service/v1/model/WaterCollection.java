
package org.cybersapien.watercollection.service.v1.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Water Collection
 * <p>
 * A water collection
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "station_id",
    "date_time",
    "collection_version",
    "collection_type",
    "collection_content",
    "collection_quantity",
    "longitude",
    "latitude",
    "processing_state",
    "measurement_version",
    "measurement_type",
    "compound_percentages"
})
public class WaterCollection implements Serializable
{

    /**
     * Collection id (uuid; no dashes; Set by service) {read only}
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("Collection id (uuid; no dashes; Set by service) {read only}")
    @Pattern(regexp = "^[0-9a-fA-F]{32}$")
    private String id;
    /**
     * Station id (uuid; no dashes; Could be personnel id or IoT id; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("station_id")
    @JsonPropertyDescription("Station id (uuid; no dashes; Could be personnel id or IoT id; Set by Reporter)")
    @Pattern(regexp = "^[0-9a-fA-F]{32}$")
    @NotNull
    private String stationId;
    /**
     * Collection time (ISO 8601; UTC; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("date_time")
    @JsonPropertyDescription("Collection time (ISO 8601; UTC; Set by Reporter)")
    @NotNull
    private Date dateTime;
    /**
     * Collection version (10 digit string; leading zeros okay; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_version")
    @JsonPropertyDescription("Collection version (10 digit string; leading zeros okay; Set by Reporter)")
    @Pattern(regexp = "^[0-9]{10}$")
    @NotNull
    private String collectionVersion;
    /**
     * (String; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_type")
    @JsonPropertyDescription("(String; Set by Reporter)")
    @Size(min = 1, max = 256)
    @NotNull
    private String collectionType;
    /**
     * Collection content (water or gas TDB) {String; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_content")
    @JsonPropertyDescription("Collection content (water or gas TDB) {String; Set by Reporter)")
    @Size(min = 1, max = 256)
    @NotNull
    private String collectionContent;
    /**
     * Quantity (in cubic centimeters; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_quantity")
    @JsonPropertyDescription("Quantity (in cubic centimeters; Set by Reporter)")
    @NotNull
    private Double collectionQuantity;
    /**
     * Longitude (in degrees; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("longitude")
    @JsonPropertyDescription("Longitude (in degrees; Set by Reporter)")
    @NotNull
    private Double longitude;
    /**
     * Latitude (in degrees; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("latitude")
    @JsonPropertyDescription("Latitude (in degrees; Set by Reporter)")
    @NotNull
    private Double latitude;
    /**
     * Processing state (fixed set of strings; NOT_STARTED (initial state), IN_PROGRESS, DONE; Set by Server) {read only}
     * 
     */
    @JsonProperty("processing_state")
    @JsonPropertyDescription("Processing state (fixed set of strings; NOT_STARTED (initial state), IN_PROGRESS, DONE; Set by Server) {read only}")
    @Size(min = 1, max = 32)
    private String processingState;
    /**
     * Measurement version (10 digit string; leading zeros okay; Set by Server) {read only}
     * 
     */
    @JsonProperty("measurement_version")
    @JsonPropertyDescription("Measurement version (10 digit string; leading zeros okay; Set by Server) {read only}")
    @Pattern(regexp = "^[0-9]{10}$")
    private String measurementVersion;
    /**
     * Measurement type (description. e.g. Volumetric Gas; Set by Server) {read only}
     * 
     */
    @JsonProperty("measurement_type")
    @JsonPropertyDescription("Measurement type (description. e.g. Volumetric Gas; Set by Server) {read only}")
    @Size(min = 1, max = 256)
    private String measurementType;
    /**
     * Water compound percentages
     * <p>
     * Percentage of various compounds in water
     * 
     */
    @JsonProperty("compound_percentages")
    @JsonPropertyDescription("Percentage of various compounds in water")
    @Valid
    private CompoundPercentages compoundPercentages;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 3941649895345368313L;

    /**
     * Collection id (uuid; no dashes; Set by service) {read only}
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Collection id (uuid; no dashes; Set by service) {read only}
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public WaterCollection withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Station id (uuid; no dashes; Could be personnel id or IoT id; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("station_id")
    public String getStationId() {
        return stationId;
    }

    /**
     * Station id (uuid; no dashes; Could be personnel id or IoT id; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("station_id")
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public WaterCollection withStationId(String stationId) {
        this.stationId = stationId;
        return this;
    }

    /**
     * Collection time (ISO 8601; UTC; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("date_time")
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Collection time (ISO 8601; UTC; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("date_time")
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public WaterCollection withDateTime(Date dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    /**
     * Collection version (10 digit string; leading zeros okay; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_version")
    public String getCollectionVersion() {
        return collectionVersion;
    }

    /**
     * Collection version (10 digit string; leading zeros okay; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_version")
    public void setCollectionVersion(String collectionVersion) {
        this.collectionVersion = collectionVersion;
    }

    public WaterCollection withCollectionVersion(String collectionVersion) {
        this.collectionVersion = collectionVersion;
        return this;
    }

    /**
     * (String; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_type")
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * (String; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_type")
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public WaterCollection withCollectionType(String collectionType) {
        this.collectionType = collectionType;
        return this;
    }

    /**
     * Collection content (water or gas TDB) {String; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_content")
    public String getCollectionContent() {
        return collectionContent;
    }

    /**
     * Collection content (water or gas TDB) {String; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_content")
    public void setCollectionContent(String collectionContent) {
        this.collectionContent = collectionContent;
    }

    public WaterCollection withCollectionContent(String collectionContent) {
        this.collectionContent = collectionContent;
        return this;
    }

    /**
     * Quantity (in cubic centimeters; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_quantity")
    public Double getCollectionQuantity() {
        return collectionQuantity;
    }

    /**
     * Quantity (in cubic centimeters; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("collection_quantity")
    public void setCollectionQuantity(Double collectionQuantity) {
        this.collectionQuantity = collectionQuantity;
    }

    public WaterCollection withCollectionQuantity(Double collectionQuantity) {
        this.collectionQuantity = collectionQuantity;
        return this;
    }

    /**
     * Longitude (in degrees; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("longitude")
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Longitude (in degrees; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("longitude")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public WaterCollection withLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    /**
     * Latitude (in degrees; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("latitude")
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Latitude (in degrees; Set by Reporter)
     * (Required)
     * 
     */
    @JsonProperty("latitude")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public WaterCollection withLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    /**
     * Processing state (fixed set of strings; NOT_STARTED (initial state), IN_PROGRESS, DONE; Set by Server) {read only}
     * 
     */
    @JsonProperty("processing_state")
    public String getProcessingState() {
        return processingState;
    }

    /**
     * Processing state (fixed set of strings; NOT_STARTED (initial state), IN_PROGRESS, DONE; Set by Server) {read only}
     * 
     */
    @JsonProperty("processing_state")
    public void setProcessingState(String processingState) {
        this.processingState = processingState;
    }

    public WaterCollection withProcessingState(String processingState) {
        this.processingState = processingState;
        return this;
    }

    /**
     * Measurement version (10 digit string; leading zeros okay; Set by Server) {read only}
     * 
     */
    @JsonProperty("measurement_version")
    public String getMeasurementVersion() {
        return measurementVersion;
    }

    /**
     * Measurement version (10 digit string; leading zeros okay; Set by Server) {read only}
     * 
     */
    @JsonProperty("measurement_version")
    public void setMeasurementVersion(String measurementVersion) {
        this.measurementVersion = measurementVersion;
    }

    public WaterCollection withMeasurementVersion(String measurementVersion) {
        this.measurementVersion = measurementVersion;
        return this;
    }

    /**
     * Measurement type (description. e.g. Volumetric Gas; Set by Server) {read only}
     * 
     */
    @JsonProperty("measurement_type")
    public String getMeasurementType() {
        return measurementType;
    }

    /**
     * Measurement type (description. e.g. Volumetric Gas; Set by Server) {read only}
     * 
     */
    @JsonProperty("measurement_type")
    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public WaterCollection withMeasurementType(String measurementType) {
        this.measurementType = measurementType;
        return this;
    }

    /**
     * Water compound percentages
     * <p>
     * Percentage of various compounds in water
     * 
     */
    @JsonProperty("compound_percentages")
    public CompoundPercentages getCompoundPercentages() {
        return compoundPercentages;
    }

    /**
     * Water compound percentages
     * <p>
     * Percentage of various compounds in water
     * 
     */
    @JsonProperty("compound_percentages")
    public void setCompoundPercentages(CompoundPercentages compoundPercentages) {
        this.compoundPercentages = compoundPercentages;
    }

    public WaterCollection withCompoundPercentages(CompoundPercentages compoundPercentages) {
        this.compoundPercentages = compoundPercentages;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public WaterCollection withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("stationId", stationId).append("dateTime", dateTime).append("collectionVersion", collectionVersion).append("collectionType", collectionType).append("collectionContent", collectionContent).append("collectionQuantity", collectionQuantity).append("longitude", longitude).append("latitude", latitude).append("processingState", processingState).append("measurementVersion", measurementVersion).append("measurementType", measurementType).append("compoundPercentages", compoundPercentages).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(dateTime).append(latitude).append(collectionVersion).append(collectionContent).append(collectionQuantity).append(collectionType).append(id).append(stationId).append(longitude).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WaterCollection) == false) {
            return false;
        }
        WaterCollection rhs = ((WaterCollection) other);
        return new EqualsBuilder().append(dateTime, rhs.dateTime).append(latitude, rhs.latitude).append(collectionVersion, rhs.collectionVersion).append(collectionContent, rhs.collectionContent).append(collectionQuantity, rhs.collectionQuantity).append(collectionType, rhs.collectionType).append(id, rhs.id).append(stationId, rhs.stationId).append(longitude, rhs.longitude).isEquals();
    }

}
