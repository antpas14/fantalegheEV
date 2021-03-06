package org.gcnc.calculate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class Response implements Serializable {
    String status;
    String message;
    List<Rank> rank;

    public static Response buildFor(String status,
                             String message,
                             List<Rank> rank) {
        return new Response(status, message, rank);
    }
}
