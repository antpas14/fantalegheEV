package org.gcnc.calculate.controller;

public enum ResponseStatus {

    SUCCESS(true),
    FAILED(false);

    private Boolean success;
    ResponseStatus(Boolean status) {
        this.success = success;
    }
}
