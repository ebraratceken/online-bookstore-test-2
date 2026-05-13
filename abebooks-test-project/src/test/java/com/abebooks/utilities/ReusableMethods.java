package com.abebooks.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ReusableMethods {

    // Elementin görünür olmasını bekle
    public static WebElement waitForVisibility(WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Elementin tıklanabilir olmasını bekle
    public static WebElement waitForClickability(WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // JavaScript ile tıkla (popup/overlay olduğunda)
    public static void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    // Sayfanın tam yüklenmesini bekle
    public static void waitForPageLoad(int seconds) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(seconds));
        wait.until(driver ->
            ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete")
        );
    }

    // Elementin metnini al (trim ile)
    public static String getText(WebElement element) {
        waitForVisibility(element, 10);
        return element.getText().trim();
    }
}
