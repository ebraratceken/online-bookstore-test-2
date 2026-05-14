package com.abebooks.pages.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



public class HomePage {

    WebDriver driver;

    // Constructor
    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    // ======================
    // LOCATORS (REAL ABEBOOKS)
    // ======================

    // Search
    private By searchBox = By.name("kn");
    private By searchButton = By.id("header-searchbox-button");

    // Navigation
    private By signInLink = By.id("sign-on");
    private By homeLogo = By.id("logo");

    // Basket (Cart)
    private By basket = By.id("basket");

    // ======================
    // ACTIONS
    // ======================

    // Open homepage
    public void openHomePage(String url) {
        driver.get(url);
    }

    // Search for a book
    public void searchBook(String bookName) {
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(bookName);

        // Option 1: click search button
        driver.findElement(searchButton).click();

        // Option 2 (alternative):
        // driver.findElement(searchBox).sendKeys(Keys.ENTER);
    }

    // Click Sign In
    public void clickSignIn() {
        driver.findElement(signInLink).click();
    }

    // Click Home Logo
    public void clickHomeLogo() {
        driver.findElement(homeLogo).click();
    }

    // Open Basket (Cart)
    public void openBasket() {
        driver.findElement(basket).click();
    }
}
