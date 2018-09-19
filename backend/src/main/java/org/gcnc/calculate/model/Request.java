package org.gcnc.calculate.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Request implements Serializable {
    private String leagueName;
    private int goalThreshold;
    private int firstGoalThreshold;

    @Override
    public String toString() {
        return "{\"leagueName\":\"" + leagueName + "\", \"firstGoalThreshold\":\"" + firstGoalThreshold + "\", \"goalThreshold\":\"" + goalThreshold + "\"}";
    }
}
