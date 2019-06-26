package com.semitop7.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Response {
    private Boolean error = false;
    private String cid;
    private Integer statusCode;
    private String errorName;
    private String errorMsg;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public static final class ResponseBuilder {
        private Response response;

        private ResponseBuilder() {
            response = new Response();
        }

        public static ResponseBuilder aResponse() {
            return new ResponseBuilder();
        }

        public ResponseBuilder error(Boolean error) {
            response.setError(error);
            return this;
        }

        public ResponseBuilder cid(String cid) {
            response.setCid(cid);
            return this;
        }

        public ResponseBuilder statusCode(Integer statusCode) {
            response.setStatusCode(statusCode);
            return this;
        }

        public ResponseBuilder errorName(String errorName) {
            response.setErrorName(errorName);
            return this;
        }

        public ResponseBuilder errorMsg(String errorMsg) {
            response.setErrorMsg(errorMsg);
            return this;
        }

        public Response build() {
            return response;
        }
    }
}