package com.instagram.bot.activity;

import com.instagram.bot.model.Competition;
import com.instagram.bot.session.BotSession;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.temporal.failure.ApplicationFailure;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import static com.instagram.bot.model.Competition.*;

@Component
@Slf4j
public class BotActivityImpl implements BotActivity {

    private final BotSession botSession;

    public BotActivityImpl(BotSession botSession) {
        this.botSession = botSession;
    }

    @SneakyThrows
    @Override
    public List<String> getLatestCompetitions() {
        try (var session = botSession.getSession("https://worldathletics.org/competition/calendar-results?hideCompetitionsWithNoResults=true")) {
            String pattern = "/competition/calendar-results/results/";
            log.info("Logging in...");
            List<WebElement> elements = session.getDriver().findElements(By.xpath("//a[contains(@href, '" + pattern + "')]"));
            return elements.stream().map(element-> element.getAttribute("href")).collect(Collectors.toList());
        }
    }

    @Override
    public Competition readResultsByCompetiton(String link)  {
        try (var session = botSession.getSession(link)) {

            var driver  = session.getDriver();

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
            return builder.results(resultsForCompetition).build();
        } catch (Exception e) {
            throw  ApplicationFailure.newNonRetryableFailure(e.getMessage(), "readResultsByCompetiton", e);
        }
    }

    @Override
    public void processByCompetition() throws Exception {
        try (var session = botSession.getSession("https://x.com/i/flow/login")) {
            var driver  = session.getDriver();
            Thread.sleep(10000);
            WebElement usernameInput = driver.findElement(By.tagName("input"));
            usernameInput.sendKeys("");
            usernameInput.sendKeys(Keys.TAB);
            WebElement focusedElement = driver.switchTo().activeElement();

            focusedElement.click();
            Thread.sleep(10000);
            focusedElement = driver.switchTo().activeElement();
            focusedElement.sendKeys("");
            Thread.sleep(10000);

        }
    }

}
