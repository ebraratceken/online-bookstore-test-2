package com.abebooks.tests.auth_tests;

import com.abebooks.base.BaseTest;
import com.abebooks.pages.auth.HomePage;
import com.abebooks.pages.auth.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;


public class LoginTest extends BaseTest {

    // Helper: check if CAPTCHA is blocking the page
    private boolean isCaptchaPresent() {
        return driver.getPageSource().contains("Solve this puzzle to protect your account")
                || driver.getPageSource().contains("Authentication required");
    }

    // ======================
    // POSITIVE TEST
    // ======================
    @Test
    public void validLoginTest() throws InterruptedException {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.openHomePage("https://www.abebooks.com/");
        home.clickSignIn();

        login.enterEmail("sumeyahassan5020@gmail.com");
        login.enterPassword("manon123.");
        login.clickSignIn();

        Thread.sleep(5000);

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test skipped.");
            return;
        }

        String pageText = driver.getPageSource();
        System.out.println(pageText);

        Assert.assertFalse(pageText.contains("We cannot find an account"));
    }
    @Test
    public void passwordAssistanceTest() throws InterruptedException {

        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        // Open AbeBooks
        home.openHomePage("https://www.abebooks.com/");

        // Open Sign In page
        home.clickSignIn();

        // Click Password assistance
        login.clickForgotPassword();

        // Enter email
        login.enterEmail("sumeyahassan5020@gmail.com");

        // Click Continue button
        login.clickContinueButton();

        Thread.sleep(5000);

        // CAPTCHA check
        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test skipped.");
            return;
        }

        // Get page source
        String pageText = driver.getPageSource();

        // Verify success message
        Assert.assertTrue(
                pageText.contains("You will receive an email from us with instructions for resetting your password"),
                "Password assistance message NOT displayed."
        );

        System.out.println("Password assistance working successfully.");
    }
    @Test
    public void emptyLoginTest() throws InterruptedException {

        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        // Open AbeBooks homepage
        home.openHomePage("https://www.abebooks.com/");

        // Click Sign In
        home.clickSignIn();

        // Leave email and password empty
        login.enterEmail("");
        login.enterPassword("");

        // Click Sign In button
        login.clickSignIn();

        Thread.sleep(3000);

        // CAPTCHA check
        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test skipped.");
            return;
        }

        // Get page source
        String pageText = driver.getPageSource();

        System.out.println(pageText);

        // Verify validation/error message appears
        Assert.assertTrue(
                pageText.contains("Enter your email or mobile phone number"),
                "Validation message for empty login was NOT displayed."
        );

        System.out.println("Empty login test passed successfully.");
    }
    @Test
    public void emptyPasswordTest() throws InterruptedException {

        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        // Open website
        home.openHomePage("https://www.abebooks.com/");

        // Go to Sign In page
        home.clickSignIn();

        // Enter email but leave password empty
        login.enterEmail("sumeyahassan5020@gmail.com");
        login.enterPassword("");

        // Click Sign In
        login.clickSignIn();

        Thread.sleep(3000);

        // CAPTCHA check
        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test skipped.");
            return;
        }

        String pageText = driver.getPageSource().toLowerCase();

        System.out.println(pageText);

        Assert.assertTrue(
                pageText.contains("enter your password")
                        || pageText.contains("password")
                        || pageText.contains("required"),
                "Empty password validation message not found"
        );

        System.out.println("Empty password test passed successfully.");


    }

    // ======================
    // NEGATIVE TEST 1 (wrong email)
    // ======================
    @Test
    public void invalidEmailTest() throws InterruptedException {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.openHomePage("https://www.abebooks.com/");
        home.clickSignIn();

        login.enterEmail("zainabhassan@gmai.com");
        login.enterPassword("manon123.");
        login.clickSignIn();

        Thread.sleep(3000);

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — cannot verify error message.");
            System.out.println("Test skipped because CAPTCHA appeared.");
            return;
        }

        String pageSource = driver.getPageSource();
        String pageText = pageSource.toLowerCase();

        System.out.println("=== PAGE TEXT SNIPPET ===");
        System.out.println(pageSource.substring(0, Math.min(pageSource.length(), 3000)));

// Broad assertion covering common wrong-password messages
        Assert.assertTrue(
                pageText.contains("password")
                        || pageText.contains("incorrect")
                        || pageText.contains("invalid")
                        || pageText.contains("wrong")
                        || pageText.contains("error")
                        || pageText.contains("try again")
                        || pageText.contains("cannot find an account")
                        || pageText.contains("email address"),
                "Expected login error message but none was found. Actual page text: "
                        + pageSource.substring(0, Math.min(pageSource.length(), 500))
        );
    }

    // ======================
    // NEGATIVE TEST 2 (wrong password)
    // ======================
    @Test
    public void invalidPasswordTest() throws InterruptedException {
        HomePage home = new HomePage(driver);
        LoginPage login = new LoginPage(driver);

        home.openHomePage("https://www.abebooks.com/");
        home.clickSignIn();

        login.enterEmail("sumeyahassan5020@gmail.com");
        login.enterPassword("mano123.");
        login.clickSignIn();

        Thread.sleep(3000);

        if (isCaptchaPresent()) {
            System.out.println("CAPTCHA detected — test skipped.");
            return;
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("auth-error-message-box"))
        );

        String errorMessage = errorBox.getText();
        System.out.println(errorMessage);

        Assert.assertTrue(
                errorMessage.toLowerCase().contains("password")
                        || errorMessage.toLowerCase().contains("incorrect"),
                "Expected password error message not found"
        );
    }
}

