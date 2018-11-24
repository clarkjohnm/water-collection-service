
package org.cybersapien.watercollection.service.v1.model.error;

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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Error
 * <p>
 * REST Error source
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "pointer"
})
public class Source implements Serializable
{

    /**
     * Path representing the point of the error. Generally a URI path element {read only}
     * (Required)
     * 
     */
    @JsonProperty("pointer")
    @JsonPropertyDescription("Path representing the point of the error. Generally a URI path element {read only}")
    @NotNull
    private String pointer;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 3658806082605682264L;

    /**
     * Path representing the point of the error. Generally a URI path element {read only}
     * (Required)
     * 
     */
    @JsonProperty("pointer")
    public String getPointer() {
        return pointer;
    }

    /**
     * Path representing the point of the error. Generally a URI path element {read only}
     * (Required)
     * 
     */
    @JsonProperty("pointer")
    public void setPointer(String pointer) {
        this.pointer = pointer;
    }

    public Source withPointer(String pointer) {
        this.pointer = pointer;
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

    public Source withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("pointer", pointer).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pointer).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Source) == false) {
            return false;
        }
        Source rhs = ((Source) other);
        return new EqualsBuilder().append(pointer, rhs.pointer).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
