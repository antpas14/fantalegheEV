package org.gcnc.calculate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Rank implements Serializable {
    private String team;
    private Double evPoints;
    private int points;
}
