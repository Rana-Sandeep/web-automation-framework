package com.framework.hooks;

import com.framework.driver.DriverFactory;
import com.framework.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    @Before(order = 0)
    public void setUp(Scenario scenario) {
        LOG.info("START scenario: {}", scenario.getName());
        DriverFactory.initDriver();
    }

    @After(order = 0)
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        try {
            if (scenario.isFailed()) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure - " + scenario.getName());
            }
        } finally {
            LOG.info("END scenario: {} -> {}", scenario.getName(), scenario.getStatus());
            DriverManager.quitDriver();
        }
    }
}
