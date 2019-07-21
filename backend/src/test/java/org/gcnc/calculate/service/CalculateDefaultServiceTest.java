package org.gcnc.calculate.service;

import org.gcnc.calculate.model.Properties;
import org.gcnc.calculate.model.Request;
import org.gcnc.calculate.model.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class CalculateDefaultServiceTest {

    @Mock
    RemoteWebDriver webDriver;

    @Mock
    Properties properties;

    @InjectMocks
    private CalculateDefaultService calculateService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        initializeProperties();
        calculateService = new CalculateDefaultService(properties, webDriver);
    }

    @Test
    public void calculateTest() throws IOException {
        // Given
        File rankingFile = new File("src/test/resources/html/ranking.html");
        File calendarFile = new File("src/test/resources/html/calendar.html");


        String ranking = new String(Files.readAllBytes(Paths.get(rankingFile.getAbsolutePath())));
        String calendar = new String(Files.readAllBytes(Paths.get(calendarFile.getAbsolutePath())));
        Mockito.when(webDriver.getPageSource())
                .thenReturn(ranking)
                .thenReturn(calendar);

        Request request = new Request("league-name");
        // When
        Response response = calculateService.calculateResponse(request);
        // Then
        Assert.assertEquals("ok", response.getStatus());
        Assert.assertEquals("Sasha FC", response.getRank().get(0).getTeam());
        Assert.assertEquals(new Double(51.00000000000001), response.getRank().get(0).getEvPoints());
    }

    @Test
    public void calculateTestFailure() throws IOException {
        // Given
        File rankingFile = new File("src/test/resources/html/ranking.html");
        File calendarFile = new File("src/test/resources/html/calendar.html");


        String ranking = new String(Files.readAllBytes(Paths.get(rankingFile.getAbsolutePath())));
        String calendar = new String(Files.readAllBytes(Paths.get(calendarFile.getAbsolutePath())));
        Mockito.when(webDriver.getPageSource()).thenThrow(NullPointerException.class);

        Request request = new Request("league-name");
        // When
        Response response = calculateService.calculateResponse(request);
        // Then
        Assert.assertEquals("err", response.getStatus());
    }



    private void initializeProperties() {
        properties.setBaseUrl("http://baseurl");
        properties.setCalendarSuffix("/calendar");
        properties.setSeleniumUrl("http://selenium");
    }
}