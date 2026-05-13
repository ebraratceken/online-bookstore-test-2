package com.abebooks.base;

import com.abebooks.utilities.ConfigReader;
import com.abebooks.utilities.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class TestBase {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = Driver.getDriver();
        driver.get(ConfigReader.getProperty("base_url"));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        Driver.quitDriver();
    }
}
