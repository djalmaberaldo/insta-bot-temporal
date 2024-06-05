package com.instagram.bot.session;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Session implements AutoCloseable {

    @Getter
    private WebDriver driver;

    public Session(String url) {
        log.info("Creating session");
        WebDriverManager.firefoxdriver().clearDriverCache().setup();
        driver = new FirefoxDriver();
        driver.get(url);
    }

    public Session getSessionByBrowser(String url, BotSession.Browser browser) {
        return null;
    }


    @Override
    public void close() throws Exception {
        log.info("Closing driver");
        driver.quit();
    }
}
