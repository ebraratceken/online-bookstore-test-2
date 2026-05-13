package com.abebooks.pages.auth;

import com.abebooks.utilities.Driver;
import com.abebooks.utilities.ReusableMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * MODÜL 1: Authentication (Kimlik Doğrulama)
 * Sorumlu: Sumaya
 * Test Case'ler: TC01, TC02, TC03
 */
public class LoginPage {

    public LoginPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // ===== LOCATOR'LAR =====

    @FindBy(linkText = "Sign in")
    public WebElement signInLink;

    @FindBy(id = "email")
    public WebElement emailInput;

    @FindBy(id = "password")
    public WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    public WebElement loginButton;

    @FindBy(css = ".error-message, .alert-danger, [class*='error']")
    public WebElement errorMessage;

    @FindBy(css = "#my-account-link, .account-nav, a[href*='MembersMainMenu']")
    public WebElement myAccountLink;

    @FindBy(linkText = "Sign Off")
    public WebElement signOffLink;

    @FindBy(css = "a[href*='SignOff']")
    public WebElement logoutLink;

    @FindBy(css = "input[name='email'], input[type='email']")
    public WebElement emailField;

    @FindBy(css = "input[name='password'], input[type='password']")
    public WebElement passwordField;

    // ===== METOTLAR =====

    /**
     * TC01 - Login sayfasına git
     */
    public void goToLoginPage() {
        ReusableMethods.waitForClickability(signInLink, 10).click();
    }

    /**
     * TC01 - Geçerli bilgilerle giriş yap
     */
    public void loginWithValidCredentials(String email, String password) {
        goToLoginPage();
        ReusableMethods.waitForVisibility(emailInput, 10).sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    /**
     * TC01 - Geçersiz bilgilerle giriş dene
     */
    public void loginWithInvalidCredentials(String email, String password) {
        goToLoginPage();
        ReusableMethods.waitForVisibility(emailInput, 10).sendKeys(email);
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    /**
     * TC02 - Boş alanlarla giriş dene
     */
    public void clickLoginWithEmptyFields() {
        goToLoginPage();
        ReusableMethods.waitForClickability(loginButton, 10).click();
    }

    /**
     * TC03 - Çıkış yap
     */
    public void logout() {
        ReusableMethods.waitForClickability(myAccountLink, 10).click();
        ReusableMethods.waitForClickability(logoutLink, 10).click();
    }

    /**
     * Hata mesajını döndür
     */
    public String getErrorMessage() {
        return ReusableMethods.getText(errorMessage);
    }

    /**
     * Kullanıcının giriş yapıp yapmadığını kontrol et
     */
    public boolean isLoggedIn() {
        try {
            ReusableMethods.waitForVisibility(myAccountLink, 5);
            return myAccountLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
