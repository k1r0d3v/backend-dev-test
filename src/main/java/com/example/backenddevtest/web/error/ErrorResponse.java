package com.example.backenddevtest.web.error;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Response body for errors.
 * <p>
 * See {@link org.springframework.boot.web.servlet.error.DefaultErrorAttributes} for more information
 * on spring default error attributes.
 */
public class ErrorResponse {
    private Date timestamp;
    private Integer status;
    private String error;
    private String path;

    private Map<String, Object> additionalAttributes;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalAttributes() {
        return additionalAttributes;
    }

    @JsonAnySetter
    public void setAdditionalAttributes(Map<String, Object> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public void addAdditionalAttribute(String name, Object value) {
        if (this.additionalAttributes == null) {
            this.additionalAttributes = new TreeMap<>();
        }

        this.additionalAttributes.put(name, value);
    }
}
