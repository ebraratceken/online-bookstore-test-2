package com.abebooks.tests.auth_tests;

import com.abebooks.base.TestBase;
import com.abebooks.pages.auth.LoginPage;
import com.abebooks.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * MODÜL 1: Authentication Test Sınıfı
 * Sorumlu: Sumaya
 *
 * TC01 – Login (Valid & Invalid)
 * TC02 – Empty Fields Validation
 * TC03 – Logout Functionality
 */
public class LoginTest extends TestBase {

    LoginPage loginPage;

    @BeforeMethod
    public void setUpPage() {
        loginPage = new LoginPage();
    }

    /**
     * TC01-A: Geçerli email ve şifre ile giriş yapılabilmeli
     * Expected: Login başarılı, kullanıcı dashboard'a yönlendirilmeli
     */
    @Test(description = "TC01-A: Valid credentials ile login")
    public void testValidLogin() {
        String email = ConfigReader.getProperty("valid_email");
        String password = ConfigReader.getProperty("valid_password");

        loginPage.loginWithValidCredentials(email, password);

        Assert.assertTrue(loginPage.isLoggedIn(),
            "Geçerli bilgilerle giriş yapıldıktan sonra kullanıcı hesabı görünmeli!");
    }

    /**
     * TC01-B: Yanlış şifre ile giriş yapılamadığı doğrulanmalı
     * Expected: Hata mesajı gösterilmeli
     */
    @Test(description = "TC01-B: Invalid credentials ile login denemesi")
    public void testInvalidLogin() {
        String email = ConfigReader.getProperty("valid_email");
        String wrongPassword = ConfigReader.getProperty("invalid_password");

        loginPage.loginWithInvalidCredentials(email, wrongPassword);

        Assert.assertFalse(loginPage.isLoggedIn(),
            "Yanlış şifre ile giriş yapılamamalı!");

        String error = loginPage.getErrorMessage();
        Assert.assertFalse(error.isEmpty(),
            "Hata mesajı ekranda görünmeli!");
    }

    /**
     * TC02: Boş alanlarla login butonuna basıldığında
     * Expected: Required field (zorunlu alan) hata mesajları gösterilmeli
     */
    @Test(description = "TC02: Boş alanlarla login denemesi")
    public void testEmptyFieldsValidation() {
        loginPage.clickLoginWithEmptyFields();

        // HTML5 validation veya custom hata mesajı kontrol
        String currentUrl = driver.getCurrentUrl();

        // Hata mesajı gösterilmeli VEYA login sayfasında kalınmalı
        boolean stayedOnLoginPage = currentUrl.contains("login") || currentUrl.contains("Login");
        boolean errorShown = false;
        try {
            errorShown = loginPage.errorMessage.isDisplayed();
        } catch (Exception ignored) {}

        Assert.assertTrue(stayedOnLoginPage || errorShown,
            "Boş alanlarla giriş denemesinde hata gösterilmeli veya sayfada kalınmalı!");
    }

    /**
     * TC03: Giriş yaptıktan sonra çıkış yapılabilmeli
     * Expected: Kullanıcı çıkış yapıldı ve yönlendirildi
     */
    @Test(description = "TC03: Logout işlevi")
    public void testLogout() {
        // Önce giriş yap
        String email = ConfigReader.getProperty("valid_email");
        String password = ConfigReader.getProperty("valid_password");
        loginPage.loginWithValidCredentials(email, password);

        Assert.assertTrue(loginPage.isLoggedIn(), "Test ön koşulu: Giriş yapılmış olmalı!");

        // Çıkış yap
        loginPage.logout();

        // Çıkış sonrası login linki tekrar görünmeli
        Assert.assertTrue(loginPage.signInLink.isDisplayed(),
            "Çıkış yapıldıktan sonra 'Sign in' linki tekrar görünmeli!");
    }
}
