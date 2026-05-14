package com.abebooks.pages.checkout;

import com.abebooks.utilities.Driver;
import com.abebooks.utilities.ReusableMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * MODÜL 4: Checkout Page Object
 * Sorumlu: Ebrar
 *
 * Gerçek AbeBooks checkout akışı:
 *
 * [GİRİŞ YAPILMIŞSA]
 *  Arama → "Add to basket" (id=add-to-basket-link-1)
 *        → Modal: "Proceed to Basket" (id=shopping-basket-modal-checkout) → tıkla
 *        → Basket sayfası → "Checkout" (data-test-id=proceed-to-checkout-button) → tıkla
 *        → <h1 class="css-edkqx3">Secure checkout</h1>  ✓
 *        → Adres formunda hiçbir şey girmeden "Add and continue" bas
 *        → <div id="alert--r4--children">Enter a first and last name</div>  ✓ PASS
 *
 * [GİRİŞ YAPILMAMIŞSA — YANLIŞ MAİL]
 *  Arama → "Add to basket" → Modal → "Proceed to Basket" → Basket → "Checkout"
 *        → Amazon login formu → yanlış e-posta gir → Continue
 *        → <div id="auth-error-message-box">We cannot find an account...</div>  ✓ PASS
 *
 * [BOŞ SEPET]
 *  Giriş yap → /checkout/basket'e git (ürün eklenmeden)
 *        → <p class="css-1qtjq54">You don't have any items...</p>  ✓ PASS
 */
public class CheckoutPage {

    public CheckoutPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // ─────────────────────────────────────────────
    // LOCATOR'LAR
    // ─────────────────────────────────────────────

    /**
     * Arama sonucundaki ilk ürünün "Add to basket" butonu
     * id="add-to-basket-link-1"
     */
    @FindBy(id = "add-to-basket-link-1")
    public WebElement addToBasketButton;

    /**
     * Sepete ekleme sonrası çıkan modalın "Proceed to Basket" butonu — ADIM 1
     * id="shopping-basket-modal-checkout"
     */
    @FindBy(id = "shopping-basket-modal-checkout")
    public WebElement proceedToBasketButton;

    /**
     * Basket sayfasındaki "Checkout" butonu — ADIM 2
     * data-test-id="proceed-to-checkout-button"
     */
    @FindBy(css = "[data-test-id='proceed-to-checkout-button']")
    public WebElement checkoutButton;

    /**
     * Secure checkout başlığı — giriş yapılmış + ürün varsa görünür
     * <h1 class="css-edkqx3">Secure checkout</h1>
     */
    @FindBy(css = "h1.css-edkqx3")
    public WebElement secureCheckoutHeading;

    /**
     * Boş sepet mesajı
     * <p class="css-1qtjq54">You don't have any items in your basket...</p>
     */
    @FindBy(css = "p.css-1qtjq54")
    public WebElement emptyBasketMessage;

    /**
     * Amazon login — e-posta alanı (login olmadan checkout'ta çıkar)
     * id="ap_email"
     */
    @FindBy(id = "ap_email")
    public WebElement amazonEmailInput;

    /**
     * Amazon login — şifre alanı
     * id="ap_password"
     */
    @FindBy(id = "ap_password")
    public WebElement amazonPasswordInput;

    /**
     * Amazon login — submit butonu (hem email hem şifre adımında kullanılır)
     * id="signInSubmit"
     */
    @FindBy(id = "signInSubmit")
    public WebElement signInSubmitButton;

    /**
     * TC10 (YENİ): Yanlış e-posta girilince çıkan Amazon auth hata mesajı kutusu
     * id="auth-error-message-box"
     * "We cannot find an account with that email address"
     */
    @FindBy(id = "auth-error-message-box")
    public WebElement authErrorMessageBox;

    /**
     * TC11 (YENİ): Adres formundaki "Add and continue" butonu
     * data-test-id="create-address-save"
     */
    @FindBy(css = "[data-test-id='create-address-save']")
    public WebElement saveAddressButton;

    /**
     * TC11 (YENİ): Adres formu boş gönderilince çıkan hata mesajı
     * id="alert--r4--children"
     * "Enter a first and last name"
     */
    @FindBy(id = "alert--r4--children")
    public WebElement addressErrorMessage;

    // ─────────────────────────────────────────────
    // METOTLAR
    // ─────────────────────────────────────────────

    /**
     * Arama sonucundaki ilk ürünü sepete ekler.
     */
    public void addFirstItemToBasket() {
        ReusableMethods.waitForClickability(addToBasketButton, 15).click();
    }

    /**
     * Adım 1: Modaldan "Proceed to Basket" butonuna tıklar → basket sayfasına gider.
     * Adım 2: Basket sayfasındaki "Checkout" butonuna tıklar → secure checkout / login formu açılır.
     */
    public void proceedToBasket() {
        // Adım 1: Modal → Proceed to Basket
        ReusableMethods.waitForClickability(proceedToBasketButton, 15).click();
        ReusableMethods.waitForPageLoad(10);

        // Adım 2: Basket sayfası → Checkout
        ReusableMethods.waitForClickability(checkoutButton, 15).click();
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC11: Checkout sonrası çıkan Amazon login formundan giriş yapar.
     * Amazon iki adımlı akış:
     *  Adım 1 → E-posta gir + Continue (signInSubmit)
     *  Adım 2 → Şifre gir  + Sign-In  (signInSubmit)
     */
    public void loginViaAmazonForm(String email, String password) {
        // Adım 1: E-posta
        ReusableMethods.waitForVisibility(amazonEmailInput, 15).clear();
        amazonEmailInput.sendKeys(email);
        ReusableMethods.waitForClickability(signInSubmitButton, 10).click();

        // Adım 2: Şifre
        ReusableMethods.waitForVisibility(amazonPasswordInput, 15).clear();
        amazonPasswordInput.sendKeys(password);
        ReusableMethods.waitForClickability(signInSubmitButton, 10).click();

        ReusableMethods.waitForPageLoad(15);
    }

    /**
     * TC10 (YENİ): Login olmadan checkout → yanlış e-posta girer ve Continue'ya basar.
     * Sadece e-posta adımını tamamlar (şifreye geçmez), hata bekleniyor.
     */
    public void enterInvalidEmailOnLoginForm(String invalidEmail) {
        ReusableMethods.waitForVisibility(amazonEmailInput, 15).clear();
        amazonEmailInput.sendKeys(invalidEmail);
        ReusableMethods.waitForClickability(signInSubmitButton, 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC11 (YENİ): Adres formunda hiçbir şey girmeden "Add and continue" butonuna basar.
     */
    public void clickSaveAddressWithoutInput() {
        ReusableMethods.waitForClickability(saveAddressButton, 15).click();
    }

    /**
     * TC12: Sepete ürün eklemeden doğrudan basket sayfasına gider.
     */
    public void navigateToBasketPage() {
        Driver.getDriver().get("https://www.abebooks.com/checkout/basket");
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC10 & TC11-adım2: "Secure checkout" başlığı görünüyor mu?
     */
    public boolean isSecureCheckoutDisplayed() {
        try {
            ReusableMethods.waitForVisibility(secureCheckoutHeading, 15);
            return secureCheckoutHeading.isDisplayed()
                    && secureCheckoutHeading.getText().toLowerCase().contains("secure checkout");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC10 (ESKİ - korundu): Login formu (signInSubmit) görünüyor mu?
     */
    public boolean isRedirectedToLoginForm() {
        try {
            ReusableMethods.waitForVisibility(signInSubmitButton, 15);
            return signInSubmitButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC10 (YENİ): Yanlış e-posta sonrası Amazon auth hata mesajı görünüyor mu?
     * id="auth-error-message-box"
     */
    public boolean isAuthErrorMessageDisplayed() {
        try {
            ReusableMethods.waitForVisibility(authErrorMessageBox, 15);
            return authErrorMessageBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC11 (YENİ): Adres formu boş gönderilince hata mesajı görünüyor mu?
     * id="alert--r4--children"
     */
    public boolean isAddressErrorMessageDisplayed() {
        try {
            ReusableMethods.waitForVisibility(addressErrorMessage, 15);
            return addressErrorMessage.isDisplayed()
                    && addressErrorMessage.getText().contains("Enter a first and last name");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC12: Boş sepet mesajı görünüyor mu?
     */
    public boolean isEmptyBasketMessageDisplayed() {
        try {
            ReusableMethods.waitForVisibility(emptyBasketMessage, 15);
            return emptyBasketMessage.isDisplayed()
                    && emptyBasketMessage.getText().contains("You don't have any items in your basket");
        } catch (Exception e) {
            return false;
        }
    }

    /** Assert mesajlarında kullanmak için mevcut URL'i döndürür. */
    public String getCurrentUrl() {
        return Driver.getDriver().getCurrentUrl();
    }
}