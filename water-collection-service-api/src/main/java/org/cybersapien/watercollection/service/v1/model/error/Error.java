
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
 * REST Error
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "source",
    "title",
    "detail"
})
public class Error implements Serializable
{

    /**
     * Error status code {read only}
     * (Required)
     * 
     */
    @JsonProperty("status")
    @JsonPropertyDescription("Error status code {read only}")
    @NotNull
    private Integer status;
    /**
     * Error
     * <p>
     * REST Error source
     * (Required)
     * 
     */
    @JsonProperty("source")
    @JsonPropertyDescription("REST Error source")
    @Valid
    @NotNull
    private Source source;
    /**
     * Error title {read only}
     * 
     */
    @JsonProperty("title")
    @JsonPropertyDescription("Error title {read only}")
    private String title;
    /**
     * Error detail {read only}
     * (Required)
     * 
     */
    @JsonProperty("detail")
    @JsonPropertyDescription("Error detail {read only}")
    @NotNull
    private String detail;
    @JsonIgnore
    @Valid
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 1877222554153420235L;

    /**
     * Error status code {read only}
     * (Required)
     * 
     */
    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    /**
     * Error status code {read only}
     * (Required)
     * 
     */
    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Error withStatus(Integer status) {
        this.status = status;
        return this;
    }

    /**
     * Error
     * <p>
     * REST Error source
     * (Required)
     * 
     */
    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    /**
     * Error
     * <p>
     * REST Error source
     * (Required)
     * 
     */
    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    public Error withSource(Source source) {
        this.source = source;
        return this;
    }

    /**
     * Error title {read only}
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Error title {read only}
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Error withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Error detail {read only}
     * (Required)
     * 
     */
    @JsonProperty("detail")
    public String getDetail() {
        return detail;
    }

    /**
     * Error detail {read only}
     * (Required)
     * 
     */
    @JsonProperty("detail")
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Error withDetail(String detail) {
        this.detail = detail;
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

    public Error withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("source", source).append("title", title).append("detail", detail).append("additionalProperties", additionalProperties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(source).append(detail).append(additionalProperties).append(title).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Error) == false) {
            return false;
        }
        Error rhs = ((Error) other);
        return new EqualsBuilder().append(source, rhs.source).append(detail, rhs.detail).append(additionalProperties, rhs.additionalProperties).append(title, rhs.title).append(status, rhs.status).isEquals();
    }

}
