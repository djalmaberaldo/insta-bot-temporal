package com.instagram.bot.activity;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InstaBotActivityImpl implements InstaBotActivity {

    private WebDriver driver;

    @SneakyThrows
    @Override
    public List<String> login() {
        String pattern = "/competition/calendar-results/results/";
        log.info("Logging in...");
        driver = getDriver();
        driver.get("https://worldathletics.org/competition/calendar-results?hideCompetitionsWithNoResults=true");
        List<WebElement> elements = driver.findElements(By.xpath("//a[contains(@href, '" + pattern + "')]"));
        var list  = elements.stream().map(element-> element.getAttribute("href")).collect(Collectors.toList());
        driver.quit();
        return list;
    }

    @Override
    public void readResults(String result) {
        driver = getDriver();
        driver.get(result);
    }

    @Override
    public void likeByFeed() {

    }

    private WebDriver getDriver() {
        WebDriverManager.firefoxdriver().clearDriverCache().setup();
        driver = new FirefoxDriver();
        return driver;
    }
}
