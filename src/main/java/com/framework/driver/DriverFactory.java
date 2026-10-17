package com.framework.driver;

import com.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.time.Duration;

/** Creates browser sessions (local or Selenium Grid) based on configuration. */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");
        String remoteUrl = ConfigReader.get("remote.url");
        boolean remote = !remoteUrl.isBlank();

        WebDriver driver;
        switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080", "--no-sandbox",
                        "--disable-dev-shm-usage", "--disable-gpu", "--remote-allow-origins=*");
                driver = remote ? remote(remoteUrl, options) : new ChromeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless");
                }
                options.addArguments("--width=1920", "--height=1080");
                driver = remote ? remote(remoteUrl, options) : new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage");
                driver = remote ? remote(remoteUrl, options) : new EdgeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout")));
        if (!headless) {
            driver.manage().window().maximize();
        }
        DriverManager.setDriver(driver);
    }

    private static WebDriver remote(String url, org.openqa.selenium.Capabilities capabilities) {
        try {
            return new RemoteWebDriver(URI.create(url).toURL(), capabilities);
        } catch (java.net.MalformedURLException e) {
            throw new IllegalArgumentException("Invalid remote.url: " + url, e);
        }
    }
}
