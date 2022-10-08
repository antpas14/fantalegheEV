package org.gcnc.calculate.controller;

import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.service.CalculateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class CalculateControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CalculateController calculateController;

    @Mock
    private CalculateService calculateService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calculateControllerSuccess() throws Exception {
        Mockito.when(calculateService.calculateResponse(Mockito.any(Request.class))).thenReturn(Mono.just(new ArrayList<>()));
        mockMvc = MockMvcBuilders.standaloneSetup(calculateController).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/calculate?league_name=abc"))
                .andExpect(status().is(200));
    }
}