package org.gcnc.calculate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

public record Rank(String team, Double evPoints, int points) {}