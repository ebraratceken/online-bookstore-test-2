package com.abebooks.pages.checkout;

import com.abebooks.utilities.Driver;
import com.abebooks.utilities.ReusableMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * MODÜL 4: Checkout & Payment (Ödeme)
 * Sorumlu: Ebrar
 * Test Case'ler: TC10, TC11, TC12
 */
public class CheckoutPage {

    public CheckoutPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // ===== LOCATOR'LAR =====

    @FindBy(css = "a[href*='checkout'], button[class*='checkout'], .checkout-btn")
    public WebElement checkoutButton;

    @FindBy(css = ".checkout-container, [class*='checkout-step'], #checkout-form")
    public WebElement checkoutContainer;

    // Login olmadan checkout denendiğinde yönlendirme mesajı veya login sayfası
    @FindBy(css = "#email, input[name='email'], .sign-in-form")
    public WebElement loginFormOnCheckout;

    // Boş sepet uyarısı
    @FindBy(css = ".empty-basket, .empty-cart, [class*='empty'], .warning-message")
    public WebElement emptyCartWarning;

    // Teslimat adresi formu
    @FindBy(css = "input[name='firstName'], #firstName")
    public WebElement firstNameInput;

    @FindBy(css = "input[name='lastName'], #lastName")
    public WebElement lastNameInput;

    @FindBy(css = "input[name='address'], #address1")
    public WebElement addressInput;

    // Ödeme başarı/hata mesajları
    @FindBy(css = ".order-confirmation, .success-message, [class*='confirmation']")
    public WebElement orderConfirmation;

    @FindBy(css = ".payment-error, .card-error, [class*='payment-error']")
    public WebElement paymentError;

    // ===== METOTLAR =====

    /**
     * TC10 - Checkout butonuna tıkla
     */
    public void clickCheckout() {
        ReusableMethods.waitForClickability(checkoutButton, 10);
        ReusableMethods.jsClick(checkoutButton);
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC10 - Checkout sayfasına ulaşıldı mı kontrol et
     */
    public boolean isOnCheckoutPage() {
        String url = Driver.getDriver().getCurrentUrl();
        return url.contains("checkout") || url.contains("Checkout");
    }

    /**
     * TC11 - Login olmadan checkout yapılmaya çalışıldı mı
     * AbeBooks'ta login zorunlu, bu yüzden login sayfasına yönlendirme bekleniyor
     */
    public boolean isRedirectedToLogin() {
        String url = Driver.getDriver().getCurrentUrl();
        try {
            boolean loginFormVisible = loginFormOnCheckout.isDisplayed();
            return url.contains("login") || url.contains("Login") || url.contains("SignIn") || loginFormVisible;
        } catch (Exception e) {
            return url.contains("login") || url.contains("Login") || url.contains("SignIn");
        }
    }

    /**
     * TC12 - Boş sepet uyarı mesajı var mı kontrol et
     */
    public boolean isEmptyCartWarningDisplayed() {
        try {
            ReusableMethods.waitForVisibility(emptyCartWarning, 5);
            return emptyCartWarning.isDisplayed();
        } catch (Exception e) {
            // URL'den de kontrol edilebilir
            return Driver.getDriver().getCurrentUrl().contains("empty");
        }
    }

    /**
     * Mevcut URL'yi döndür
     */
    public String getCurrentUrl() {
        return Driver.getDriver().getCurrentUrl();
    }
}
