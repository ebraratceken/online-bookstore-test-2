package com.abebooks.pages.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // ======================
    // LOCATORS
    // ======================

    private By emailField = By.id("ap_email");
    private By passwordField = By.id("ap_password");
    private By signInButton = By.id("signInSubmit");

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickSignIn() {
        driver.findElement(signInButton).click();
    }

    public void clickContinueButton() {
        driver.findElement(By.id("continue")).click();
    }

    public void clickForgotPassword() {
        driver.findElement(By.id("auth-fpp-link-bottom")).click();
    }

    public void loginWithValidCredentials(String validEmail, String validPassword) {

        enterEmail(validEmail);

        enterPassword(validPassword);

        clickSignIn();
    }
}
