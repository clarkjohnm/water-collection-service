
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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Water compound percentages
 * <p>
 * Percentage of various compounds in water
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "voc",
    "pesticides",
    "metals",
    "bacteria"
})
public class CompoundPercentages implements Serializable
{

    /**
     * Object id (uuid; no dashes; Set by service) {read only}
     * (Required)
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("Object id (uuid; no dashes; Set by service) {read only}")
    @Pattern(regexp = "^[0-9a-fA-F]{32}$")
    @NotNull
    private String id;
    /**
     * VOC Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("voc")
    @JsonPropertyDescription("VOC Percentage {read only}")
    @NotNull
    private Double voc;
    /**
     * Pesticide Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("pesticides")
    @JsonPropertyDescription("Pesticide Percentage {read only}")
    @NotNull
    private Double pesticides;
    /**
     * Metal Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("metals")
    @JsonPropertyDescription("Metal Percentage {read only}")
    @NotNull
    private Double metals;
    /**
     * Bacteria Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("bacteria")
    @JsonPropertyDescription("Bacteria Percentage {read only}")
    @NotNull
    private Double bacteria;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8408824841112022042L;

    /**
     * Object id (uuid; no dashes; Set by service) {read only}
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Object id (uuid; no dashes; Set by service) {read only}
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public CompoundPercentages withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * VOC Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("voc")
    public Double getVoc() {
        return voc;
    }

    /**
     * VOC Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("voc")
    public void setVoc(Double voc) {
        this.voc = voc;
    }

    public CompoundPercentages withVoc(Double voc) {
        this.voc = voc;
        return this;
    }

    /**
     * Pesticide Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("pesticides")
    public Double getPesticides() {
        return pesticides;
    }

    /**
     * Pesticide Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("pesticides")
    public void setPesticides(Double pesticides) {
        this.pesticides = pesticides;
    }

    public CompoundPercentages withPesticides(Double pesticides) {
        this.pesticides = pesticides;
        return this;
    }

    /**
     * Metal Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("metals")
    public Double getMetals() {
        return metals;
    }

    /**
     * Metal Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("metals")
    public void setMetals(Double metals) {
        this.metals = metals;
    }

    public CompoundPercentages withMetals(Double metals) {
        this.metals = metals;
        return this;
    }

    /**
     * Bacteria Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("bacteria")
    public Double getBacteria() {
        return bacteria;
    }

    /**
     * Bacteria Percentage {read only}
     * (Required)
     * 
     */
    @JsonProperty("bacteria")
    public void setBacteria(Double bacteria) {
        this.bacteria = bacteria;
    }

    public CompoundPercentages withBacteria(Double bacteria) {
        this.bacteria = bacteria;
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

    public CompoundPercentages withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("voc", voc).append("pesticides", pesticides).append("metals", metals).append("bacteria", bacteria).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bacteria).append(pesticides).append(metals).append(id).append(voc).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CompoundPercentages) == false) {
            return false;
        }
        CompoundPercentages rhs = ((CompoundPercentages) other);
        return new EqualsBuilder().append(bacteria, rhs.bacteria).append(pesticides, rhs.pesticides).append(metals, rhs.metals).append(id, rhs.id).append(voc, rhs.voc).isEquals();
    }

}
