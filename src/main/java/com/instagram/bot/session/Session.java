package com.instagram.bot.session;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;

public class Session implements AutoCloseable {

    @Getter
    private WebDriver driver;

    public Session(String url) {
        WebDriverManager.firefoxdriver().clearDriverCache().setup();
        driver = new FirefoxDriver();
        driver.get(url);
    }



    @Override
    public void close() throws Exception {
        driver.quit();
    }
}
