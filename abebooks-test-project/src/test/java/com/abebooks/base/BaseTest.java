package com.abebooks.base;

import com.abebooks.utilities.ConfigReader;
import com.abebooks.utilities.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {

        // Get driver from Driver utility class
        driver = Driver.getDriver();

        // Open website from config.properties
        driver.get(ConfigReader.getProperty("base_url"));

        // Maximize browser
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {

        // Close browser safely
        if (driver != null) {
            Driver.quitDriver();
        }
    }
}