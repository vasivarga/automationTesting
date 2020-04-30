package com.test.jbehave.utils;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Driver {

    private Driver() {
    }

    public static WebDriver driver;

    public static void init() {
        //Configurations for file properties using
        Properties properties = new Properties();
        FileInputStream propFile;
        try {
            propFile = new FileInputStream("test.properties");
            properties.load(propFile);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        @SuppressWarnings("unchecked")
        Enumeration<String> e = (Enumeration<String>) properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            System.setProperty(key, properties.getProperty(key));
        }

        String s = System.getProperty("test.browser");
        if (s.equals("firefox")) {
            driver = new FirefoxDriver();

        } else if (s.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            driver = new ChromeDriver();

        } else if (s.equals("chrome_hub")) {
            DesiredCapabilities capability = DesiredCapabilities.chrome();
            try {
                driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capability);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
                throw new AssertionError("Unexpected error during remote WebDriver setup");
            }

        } else {
            throw new AssertionError("Unsupported browser: " + System.getProperty("test.browser"));
        }

        //Configurations for synchronization
        driver.manage().timeouts().implicitlyWait(
                Integer.parseInt(System.getProperty("test.timeout")),
                TimeUnit.SECONDS
        );
    }


    public static void tearDown() {
        driver.quit();
    }
}