package com.bookstore.pages.checkout;

import com.bookstore.utils.DriverManager;
import com.bookstore.utils.WaitUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object: Checkout
 * Path: src/main/java/com/bookstore/pages/checkout/CheckoutPage.java
 *
 * Migrated from legacy com.abebooks.pages.checkout.CheckoutPage.
 * - ReusableMethods  → WaitUtils
 * - Driver.getDriver() → DriverManager.getDriver()
 *
 * Covers: TC10, TC11, TC13
 */
public class CheckoutPage {

    public CheckoutPage() {
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    // ─────────────────────────────────────────────
    // LOCATORS
    // ─────────────────────────────────────────────

    /** Header "Sign in" link — id="sign-on" */
    @FindBy(id = "sign-on")
    public WebElement signOnLink;

    /** Amazon login — email field — id="ap_email" */
    @FindBy(id = "ap_email")
    public WebElement emailInput;

    /** Amazon login — password field — id="ap_password" */
    @FindBy(id = "ap_password")
    public WebElement passwordInput;

    /** Amazon login — submit button — id="signInSubmit" */
    @FindBy(id = "signInSubmit")
    public WebElement signInSubmitButton;

    /** Search box — name="kn" */
    @FindBy(name = "kn")
    public WebElement searchBox;

    /** Search button — id="header-searchbox-button" */
    @FindBy(id = "header-searchbox-button")
    public WebElement searchButton;

    /** First product "Add to basket" button — id="add-to-basket-link-1" */
    @FindBy(id = "add-to-basket-link-1")
    public WebElement addToBasketButton;

    /** Modal "Proceed to Basket" button — id="shopping-basket-modal-checkout" */
    @FindBy(id = "shopping-basket-modal-checkout")
    public WebElement proceedToBasketButton;

    /** Basket page "Checkout" button — data-test-id="proceed-to-checkout-button" */
    @FindBy(css = "[data-test-id='proceed-to-checkout-button']")
    public WebElement checkoutButton;

    /** Secure checkout heading — h1.css-edkqx3 */
    @FindBy(css = "h1.css-edkqx3")
    public WebElement secureCheckoutHeading;

    /** TC13: Address form "Add and continue" button — data-test-id="create-address-save" */
    @FindBy(css = "[data-test-id='create-address-save']")
    public WebElement saveAddressButton;

    /** TC13: Address form error message — id="alert--r4--children" */
    @FindBy(id = "alert--r4--children")
    public WebElement addressErrorMessage;

    // ─────────────────────────────────────────────
    // ACTIONS
    // ─────────────────────────────────────────────

    /**
     * TC10 & TC13: sign-on → email → password → signInSubmit
     */
    public void signIn(String email, String password) {
        WaitUtils.waitForClickability(signOnLink, 10).click();
        WaitUtils.waitForVisibility(emailInput, 15).clear();
        emailInput.sendKeys(email);
        WaitUtils.waitForVisibility(passwordInput, 15).clear();
        passwordInput.sendKeys(password);
        WaitUtils.waitForClickability(signInSubmitButton, 10).click();
        WaitUtils.waitForPageLoad(15);
    }

    /**
     * TC11 & TC12: Adds only the first search result to the basket.
     * A search must have been performed beforehand.
     */
    public void addFirstItemToBasket() {
        WaitUtils.waitForClickability(addToBasketButton, 15).click();
    }

    /**
     * TC11: Modal → Proceed to Basket → Checkout (two steps in sequence)
     */
    public void proceedToBasket() {
        WaitUtils.waitForClickability(proceedToBasketButton, 15).click();
        WaitUtils.waitForPageLoad(10);
        WaitUtils.waitForClickability(checkoutButton, 15).click();
        WaitUtils.waitForPageLoad(10);
    }

    /**
     * TC10 & TC13: Search for a keyword and add the first result to basket.
     */
    public void searchAndAddToBasket(String keyword) {
        WaitUtils.waitForClickability(searchBox, 10).clear();
        searchBox.sendKeys(keyword);
        WaitUtils.waitForClickability(searchButton, 10).click();
        WaitUtils.waitForPageLoad(10);
        WaitUtils.waitForClickability(addToBasketButton, 15).click();
    }

    /**
     * TC10 & TC13: Modal → Proceed to Basket → Checkout
     */
    public void proceedToCheckout() {
        WaitUtils.waitForClickability(proceedToBasketButton, 15).click();
        WaitUtils.waitForPageLoad(10);
        WaitUtils.waitForClickability(checkoutButton, 15).click();
        WaitUtils.waitForPageLoad(10);
    }

    /**
     * TC11: Sign in via the Amazon login form (email → password → submit).
     */
    public void loginViaAmazonForm(String email, String password) {
        WaitUtils.waitForVisibility(emailInput, 15).clear();
        emailInput.sendKeys(email);
        WaitUtils.waitForVisibility(passwordInput, 15).clear();
        passwordInput.sendKeys(password);
        WaitUtils.waitForClickability(signInSubmitButton, 10).click();
        WaitUtils.waitForPageLoad(15);
    }

    /**
     * TC11: Navigate directly to the basket page after login.
     */
    public void navigateToBasketPage() {
        DriverManager.getDriver().get("https://www.abebooks.com/checkout/basket");
        WaitUtils.waitForPageLoad(10);
    }

    // ─────────────────────────────────────────────
    // ASSERTIONS / PREDICATES
    // ─────────────────────────────────────────────

    /**
     * TC10 & TC11: Returns true if "Secure checkout" heading is visible.
     */
    public boolean isSecureCheckoutDisplayed() {
        try {
            WaitUtils.waitForVisibility(secureCheckoutHeading, 15);
            return secureCheckoutHeading.isDisplayed()
                    && secureCheckoutHeading.getText().toLowerCase().contains("secure checkout");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC11: Returns true if the Amazon login form (signInSubmit) is visible.
     */
    public boolean isRedirectedToLoginForm() {
        try {
            WaitUtils.waitForVisibility(signInSubmitButton, 15);
            return signInSubmitButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC13: Returns true if the address validation error message is visible
     * and contains the expected text.
     */
    public boolean isAddressErrorMessageDisplayed() {
        try {
            WaitUtils.waitForVisibility(addressErrorMessage, 15);
            return addressErrorMessage.isDisplayed()
                    && addressErrorMessage.getText().contains("Enter a first and last name");
        } catch (Exception e) {
            return false;
        }
    }


//yorum


    public String getCurrentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }
}