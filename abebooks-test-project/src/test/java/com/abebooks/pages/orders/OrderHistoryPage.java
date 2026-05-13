package com.abebooks.pages.orders;

import com.abebooks.utilities.Driver;
import com.abebooks.utilities.ReusableMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * MODÜL 5: Order History (Sipariş Geçmişi)
 * Test Case'ler: TC13, TC14, TC15
 */
public class OrderHistoryPage {

    private org.openqa.selenium.WebDriver driver = Driver.getDriver();

    public OrderHistoryPage() {
        PageFactory.initElements(driver, this);
    }

    // ===== LOGIN LOCATOR'LARI =====

    @FindBy(id = "sign-on")
    public WebElement signOnLink;

    @FindBy(id = "ap_email")
    public WebElement emailField;

    @FindBy(id = "ap_password")
    public WebElement passwordField;

    @FindBy(id = "signInSubmit")
    public WebElement signInSubmitButton;

    @FindBy(id = "my-account")
    public WebElement myAccountLink;

    // ===== ORDERS SAYFASI LOCATOR'LARI =====

    @FindBy(id = "order-search-form")
    public WebElement orderSearchForm;

    @FindBy(id = "order-search-text")
    public WebElement orderSearchInput;

    @FindBy(css = "[data-test-id='order-search-submit-button']")
    public WebElement orderSearchSubmitButton;

    @FindBy(css = "table tr td a")
    public List<WebElement> orderLinks;

    // ===== METOTLAR =====

    /**
     * TC13 - Login işlemi
     * AbeBooks: önce email → submit, sonra password → submit
     */
    public void login(String email, String password) {
        driver.get("https://www.abebooks.com");
        ReusableMethods.waitForClickability(signOnLink, 10).click();

        ReusableMethods.waitForVisibility(emailField, 10);
        emailField.clear();
        emailField.sendKeys(email);
        ReusableMethods.waitForClickability(signInSubmitButton, 10).click();

        ReusableMethods.waitForVisibility(passwordField, 10);
        passwordField.clear();
        passwordField.sendKeys(password);
        ReusableMethods.waitForClickability(signInSubmitButton, 10).click();

        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC13 - My Purchases sayfasına git
     */
    public void goToOrderHistory() {
        driver.get("https://www.abebooks.com/my-account/purchases/");
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC13 - Sayfa yüklendi mi? (order-search-form görünüyor mu)
     */
    public boolean isOrdersPageLoaded() {
        try {
            ReusableMethods.waitForVisibility(orderSearchForm, 10);
            return orderSearchForm.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC13 - Siparişler listeleniyor mu?
     */
    public boolean areOrdersDisplayed() {
        try {
            return !orderLinks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC14 - İlk siparişe tıkla
     */
    public void clickFirstOrder() {
        ReusableMethods.waitForClickability(orderLinks.get(0), 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC14 - Sipariş detay sayfasında mı?
     */
    public boolean isOrderDetailPageLoaded() {
        String url = driver.getCurrentUrl();
        return url.contains("BuyerOrderTrack")
                || url.contains("order")
                || url.contains("purchase");
    }

    /**
     * TC15 - Geçersiz sipariş numarasıyla arama yap
     */
    public void searchByOrderNumber(String orderNumber) {
        ReusableMethods.waitForVisibility(orderSearchInput, 10);
        orderSearchInput.clear();
        orderSearchInput.sendKeys(orderNumber);
        ReusableMethods.waitForClickability(orderSearchSubmitButton, 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC15 - "No results were found matching your search criteria." mesajı var mı?
     */
    public boolean isNoResultsMessageDisplayed() {
        try {
            ReusableMethods.waitForPageLoad(5);
            String bodyText = driver.findElement(By.tagName("body")).getText();
            return bodyText.contains("No results were found matching your search criteria")
                    || bodyText.contains("no results")
                    || orderLinks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}