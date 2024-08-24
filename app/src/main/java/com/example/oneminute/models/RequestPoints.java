package com.example.oneminute.models;

import com.google.firebase.Timestamp;

public class RequestPoints {

    private String operationId;
    private String userId;
    private String code;
    private long amount;
    private int state;// 1 on hold  2 accepted  3 rejected
    private Timestamp timestamp;

    private String userName;

    private boolean recyclerState;

    private String errorCode;

    public String getUserName() {
        return userName;
    }

    public boolean isRecyclerState() {
        return recyclerState;
    }

    public void setRecyclerState(boolean recyclerState) {
        this.recyclerState = recyclerState;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public RequestPoints() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
