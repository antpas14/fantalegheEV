package org.gcnc.calculate.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Request implements Serializable {
    private String leagueName;
    public Request(String leagueName) {
        this.leagueName = leagueName;
    }
    public static Request buildFor(String leagueName) {
        return new Request(leagueName);
    }

}
