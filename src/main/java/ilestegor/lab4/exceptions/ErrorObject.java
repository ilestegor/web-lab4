package ilestegor.lab4.exceptions;

import java.util.Date;

public class ErrorObject {
    private Integer statusCode;
    private String detailMessage;
    private Date timeStamp;

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return detailMessage;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
