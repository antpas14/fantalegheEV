package org.gcnc.calculate.controller;

import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.Response;

public interface CalculateService {
    Response calculateResponse(Request req);
}
