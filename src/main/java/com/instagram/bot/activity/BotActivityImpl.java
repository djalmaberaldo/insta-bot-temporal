package com.instagram.bot.activity;

import com.instagram.bot.database.MongoConnection;
import com.instagram.bot.model.Competition;
import com.instagram.bot.session.BotSession;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import io.temporal.failure.ApplicationFailure;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import static com.instagram.bot.model.Competition.*;

@Component
@Slf4j
public class BotActivityImpl implements BotActivity {

    private final BotSession botSession;
    private final String urlCompetitions;
    private final MongoConnection mongoConnection;

    public BotActivityImpl(BotSession botSession,
                           @Value("${url.competitions}") String urlCompetitions,
                           MongoConnection mongoConnection) {
        this.botSession = botSession;
        this.urlCompetitions = urlCompetitions;
        this.mongoConnection = mongoConnection;
    }

    @SneakyThrows
    @Override
    public List<String> getLatestCompetitions() {
        try (var session = botSession.getSession(urlCompetitions)) {
            var driver  = session.getDriver();

            String pattern = "/competition/calendar-results/results/";
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            log.info("Logging in...");
            List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//a[contains(@href, '" + pattern + "')]"))));
            return elements.stream().map(element-> element.getAttribute("href")).collect(Collectors.toList());
        } catch (Exception e) {
            throw ApplicationFailure.newFailure(e.getMessage(), "getLatestCompetitions", e);
        }
    }

    @Override
    public Competition readResultsByCompetiton(String link)  {
        try (var session = botSession.getSession(link)) {

            var driver  = session.getDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            Map<String, List<Result>> resultsForCompetition = new HashMap<>();
            WebElement h1 = wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.tagName("h1"))));

            List<WebElement> eventsHtml = driver.findElements(By.className("EventResults_eventResult__3oyX4"));
            var builder = builder();
            log.info("Events were found: {}", eventsHtml.size());

            for (WebElement element : eventsHtml) {
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
            return builder.uuid(UUID.randomUUID()).name(h1.getText()).results(resultsForCompetition).build();
        } catch (Exception e) {
            throw  ApplicationFailure.newFailure(e.getMessage(), "readResultsByCompetiton", e);
        }
    }

    @Override
    public void processByCompetition(Competition competition) {

        log.info("Processing competition name: {}", competition.name());
        var database = mongoConnection.getMongoDatabase("Bot-TF-Competitions");

        // Get a collection
        MongoCollection<Document> collection = database.getCollection("Competitions-Results");
        log.info("Collection name: {}", collection.getNamespace().getCollectionName());

        Document document = new Document();
        document.append("name", competition.name);
        document.append("results", competition.results);
        collection.insertOne(document);
    }

}
