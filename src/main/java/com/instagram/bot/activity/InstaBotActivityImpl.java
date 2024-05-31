package com.instagram.bot.activity;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Component;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class InstaBotActivityImpl implements InstaBotActivity {

    private WebDriver driver;

    @SneakyThrows
    @Override
    public void login() {

        log.info("Logging in...");
        WebDriverManager.firefoxdriver().clearDriverCache().setup();
        driver = new FirefoxDriver();

        driver.get("https://worldathletics.org/competition/calendar-results?hideCompetitionsWithNoResults=true");
        driver.quit();
    }

    @Override
    public void likeByTags(String... tags) {

    }

    @Override
    public void likeByFeed() {

    }
}
