package org.gcnc.calculate.controller;

import org.gcnc.calculate.exceptions.RemoteSiteException;
import org.gcnc.calculate.model.*;
import org.gcnc.calculate.model.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CalculateDefaultService implements CalculateService {
    @Autowired
    Properties properties;

    private final Logger logger = LoggerFactory.getLogger(CalculateDefaultService.class);

    @Override
    public Response calculateResponse(Request req) {
        logger.info("New request for league " + req.getLeagueName());
        Map<String, Integer> points;
        Map<String, Double> evPoints;
        try {
            points = getPoints(req.getLeagueName());
            evPoints = calculateEVRank(getResults(req.getLeagueName()));
        } catch (RemoteSiteException e) {
            logger.error("Exception while retrieving data from remote URL");
            return Response.buildFor("err", "Exception while retrieving data", new ArrayList<>());
        }
        if (evPoints.size() > 0) {
            List<Rank> rank = evPoints.keySet().stream()
                    .map(t -> new Rank(t, evPoints.get(t), points.get(t)))
                    .sorted((r1, r2) -> r2.getPoints() - r1.getPoints())
                    .collect(Collectors.toList());
            return Response.buildFor("ok", "Successfully calculated ", rank);
        } else {
            return Response.buildFor("err", "No data retrieved", new ArrayList<>());
        }
    }

    private Map<String, Double> calculateEVRank(Map<Integer, List<TeamResult>> results) {

        final Map<String, Double> evRank = new HashMap<>();
        results.get(1)
                .forEach(tr -> evRank.put(tr.getTeam(), 0D));
        final int combinations = results.get(1).size() - 1;

        results.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i++) {
                TeamResult t1 = v.get(i);
                double points = 0D;
                for (int j = 0; j < v.size(); j++) {
                    if (i != j) {
                        TeamResult t2 = v.get(j);
                        points += calculateMatchPoints(t1, t2);
                    }
                }
                evRank.put(t1.getTeam(), (points / combinations) + evRank.get(t1.getTeam()));
            }
        });
        return evRank;
    }

    private Double calculateMatchPoints(TeamResult t1, TeamResult t2){
        if (t1.getGoal() > t2.getGoal()) {
            return 3D;
        } else if (t1.getGoal() == t2.getGoal()) {
            return 1D;
        } else {
            return 0D;
        }
    }


    private Map<Integer, List<TeamResult>> getResults(String leagueName) throws RemoteSiteException {
        String url = properties.getBaseUrl() + leagueName + properties.getCalendarSuffix();
        String content = getWebPage(url);
        Document doc = Jsoup.parse(content);
        Elements calendarDays = doc.select(".raised-2");
        return createMap(calendarDays);
    }

    private String getWebPage(String url) throws RemoteSiteException {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(Arrays.asList("headless", "no-sandbox", "window-size=1200x800"));
            WebDriver webDriver = new RemoteWebDriver(new URL(properties.getSeleniumUrl()),  options);
            logger.info("Successfully created webdriver");
            webDriver.get(url);
            String content = webDriver.getPageSource();
            webDriver.close();
            webDriver.quit();
            return content;
        } catch (MalformedURLException e) {
            logger.error("Cannot initialize webdriver: {} ", e.getMessage());
            throw new RemoteSiteException();
        }
    }

    private Map<Integer, List<TeamResult>> createMap(Elements calendarDays) {
        AtomicInteger counter = new AtomicInteger();
        return calendarDays.stream()
            .map(c-> c.select(".match"))
            .map(m -> m.select(".team").stream()
                    .filter(t -> t.select(".team-score").size() > 0 && !t.select(".team-score").get(0).text().equals(""))
                    .map(t -> new TeamResult(t.select(".team-name").get(0).text(), Integer.parseInt(t.select(".team-score").get(0).text())))
                    .collect(Collectors.toList()))
            .filter(l -> !l.isEmpty())
            .collect(Collectors.toMap(m-> counter.incrementAndGet(), m -> m));
    }

    private Map<String, Integer> getPoints(String leagueName) throws RemoteSiteException {
        try {
            String url = properties.getBaseUrl() + leagueName;
            String content = getWebPage(url);
            Document doc = Jsoup.parse(content);
            return doc.select(".top-ten").get(0).children().get(1).children().stream()
                    .collect(Collectors.toMap(
                            e-> e.children().get(2).children().get(0).children().get(0).html(),
                            e -> Integer.parseInt(e.children().get(10).children().get(0).html()))
                    );
        } catch (Exception e) {
            throw new RemoteSiteException();
        }
    }

}
