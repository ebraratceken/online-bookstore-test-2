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
 *
 * AbeBooks login akışı:
 *  1. Header'daki "Sign in" linkine tıkla  → id="sign-on"
 *  2. Amazon login formu açılır:
 *     - Email : id="ap_email"
 *     - Submit: id="signInSubmit"
 *     - Şifre : id="ap_password"
 *     - Submit: id="signInSubmit"
 */
public class LoginPage {

    public LoginPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // ===== LOCATOR'LAR =====

    /** Header "Sign in" linki — id="sign-on" */
    @FindBy(id = "sign-on")
    public WebElement signInLink;

    /** Amazon login — e-posta alanı — id="ap_email" */
    @FindBy(id = "ap_email")
    public WebElement emailInput;

    /** Amazon login — şifre alanı — id="ap_password" */
    @FindBy(id = "ap_password")
    public WebElement passwordInput;

    /** Amazon login submit butonu — id="signInSubmit" */
    @FindBy(id = "signInSubmit")
    public WebElement signInSubmit;

    /**
     * Orijinal loginButton alanı korundu — diğer modüller için
     * id="signInSubmit"
     */
    @FindBy(id = "signInSubmit")
    public WebElement loginButton;

    /** Hata mesajı — LoginTest errorMessage ile erişiyor */
    @FindBy(id = "auth-error-message-box")
    public WebElement errorMessage;

    /** Hata mesajı alternatif isim */
    @FindBy(id = "auth-error-message-box")
    public WebElement errorMessageBox;

    /** My Account linki — login kontrolü + logout için */
    @FindBy(css = "a[href*='MembersMainMenu']")
    public WebElement myAccountLink;

    /** Sign Off linki — orijinal koddan korundu */
    @FindBy(linkText = "Sign Off")
    public WebElement signOffLink;

    /** Logout linki — href ile */
    @FindBy(css = "a[href*='SignOff']")
    public WebElement logoutLink;

    /** E-posta alanı alternatif locator — orijinal koddan korundu */
    @FindBy(css = "input[name='email'], input[type='email']")
    public WebElement emailField;

    /** Şifre alanı alternatif locator — orijinal koddan korundu */
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
     * Amazon iki adımlı: email → Continue → şifre → Sign-In
     */
    public void loginWithValidCredentials(String email, String password) {
        goToLoginPage();

        // Adım 1: E-posta
        ReusableMethods.waitForVisibility(emailInput, 15).clear();
        emailInput.sendKeys(email);
        ReusableMethods.waitForClickability(signInSubmit, 10).click();

        // Adım 2: Şifre
        ReusableMethods.waitForVisibility(passwordInput, 15).clear();
        passwordInput.sendKeys(password);
        ReusableMethods.waitForClickability(signInSubmit, 10).click();

        ReusableMethods.waitForPageLoad(15);
    }

    /**
     * TC01 - Geçersiz bilgilerle giriş dene
     */
    public void loginWithInvalidCredentials(String email, String password) {
        loginWithValidCredentials(email, password);
    }

    /**
     * TC02 - Boş alanlarla giriş dene
     */
    public void clickLoginWithEmptyFields() {
        goToLoginPage();
        ReusableMethods.waitForClickability(signInSubmit, 10).click();
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
        try {
            return ReusableMethods.getText(errorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Kullanıcının giriş yapıp yapmadığını kontrol et
     */
    public boolean isLoggedIn() {
        try {
            ReusableMethods.waitForVisibility(myAccountLink, 8);
            return myAccountLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}