package com.instagram.bot.activity;

import com.instagram.bot.model.Competition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import static com.instagram.bot.model.Competition.*;

@Component
@Slf4j
public class InstaBotActivityImpl implements InstaBotActivity {

    private WebDriver driver;

    @SneakyThrows
    @Override
    public List<String> getLatestCompetitions() {
        String pattern = "/competition/calendar-results/results/";
        log.info("Logging in...");
        getDriver("https://worldathletics.org/competition/calendar-results?hideCompetitionsWithNoResults=true");
        List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@href, '" + pattern + "')]"));
        var list  = elements.stream().map(element-> element.getAttribute("href")).collect(Collectors.toList());
        closeDriver();
        return list;
    }

    @Override
    public Competition readResultsByCompetiton(String link) {
        getDriver(link);
        Map<String, List<Result>> resultsForCompetition = new HashMap<>();
        List<WebElement> eventsHtml = driver.findElements(By.className("EventResults_eventResult__3oyX4"));
        var builder = builder();
        log.info("Events were found: {}", eventsHtml.size());

        for (WebElement element : eventsHtml) {
            log.info("Event: {}" , element.findElement(By.tagName("h2")).getText());
            String eventName = element.findElement(By.tagName("h2")).getText();
            WebElement tbody = element.findElement(By.tagName("tbody"));
            List<WebElement> rows = tbody.findElements(By.tagName("tr"));

            List<Result> results = rows.stream()
                    .map(row -> {
                        List<WebElement> rowTds = row.findElements(By.tagName("td"));
                        return Result.builder()
                                .position(rowTds.get(0).getText())
                                .athlete(rowTds.get(1).getText())
                                .country(rowTds.get(3).findElement(By.className("Flags_name__28uFw")).getText())
                                .mark(rowTds.get(4).getText())
                                .build();
                    }).collect(Collectors.toList());

            log.info("Rows were found: {}", rows.size());
            resultsForCompetition.put(eventName, results);
        }
        closeDriver();
        builder.results(resultsForCompetition);
        return builder.build();
    }

    @Override
    public void processByCompetition() {

    }

    private void getDriver(String url) {
        WebDriverManager.firefoxdriver().clearDriverCache().setup();
        driver = new FirefoxDriver();
        driver.get(url);
    }

    private void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
