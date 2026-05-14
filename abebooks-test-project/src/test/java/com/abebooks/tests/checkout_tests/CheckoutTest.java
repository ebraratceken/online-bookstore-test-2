package com.abebooks.tests.checkout_tests;

import com.abebooks.base.TestBase;
import com.abebooks.pages.auth.LoginPage;
import com.abebooks.pages.product.SearchPage;
import com.abebooks.pages.checkout.CheckoutPage;
import com.abebooks.utilities.ConfigReader;
import com.abebooks.utilities.ReusableMethods;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * MODÜL 4: Checkout & Payment Test Sınıfı
 * Sorumlu: Ebrar
 *
 * ─────────────────────────────────────────────────────────────
 * TC10 – Geçerli checkout akışı (giriş yapılmış + ürün var)
 *   PASS kriteri: <h1 class="css-edkqx3">Secure checkout</h1> görünür
 *
 * TC11 – Login olmadan checkout (giriş yapılmamış + ürün var)
 *   PASS kriteri 1: Amazon login formu (signInSubmit) görünür
 *   PASS kriteri 2: Giriş sonrası "Secure checkout" görünür
 *
 * TC12 – Boş sepette checkout (giriş yapılmış + ürün yok)
 *   PASS kriteri: "You don't have any items in your basket." görünür
 * ─────────────────────────────────────────────────────────────
 */
public class CheckoutTest extends TestBase {

    CheckoutPage checkoutPage;
    SearchPage searchPage;
    LoginPage loginPage;

    @BeforeMethod
    public void setUpPage() {
        checkoutPage = new CheckoutPage();
        searchPage   = new SearchPage();
        loginPage    = new LoginPage();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC10: Geçerli checkout akışı
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * AKIŞ:
     *  1. Geçerli bilgilerle giriş yap (Amazon iki adımlı: email → şifre).
     *  2. Harry Potter ara → ilk ürünü sepete ekle (Add to basket).
     *  3. Açılan modalda "Proceed to Basket" butonuna tıkla.
     *  4. <h1 class="css-edkqx3">Secure checkout</h1> görünür → PASS
     */
    @Test(description = "TC10: Geçerli checkout akışı — Secure checkout sayfasına ulaşılmalı")
    public void testCheckoutFlowValid() {

        // 1. Giriş yap
        loginPage.loginWithValidCredentials(
                ConfigReader.getProperty("valid_email"),
                ConfigReader.getProperty("valid_password")
        );

        // 2. Arama yap ve ilk ürünü sepete ekle
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        checkoutPage.addFirstItemToBasket();

        // 3. "Proceed to Basket" butonuna tıkla
        checkoutPage.proceedToBasket();

        // 4. ASSERT: "Secure checkout" başlığı görünmeli
        Assert.assertTrue(
                checkoutPage.isSecureCheckoutDisplayed(),
                "TC10 FAILED: 'Secure checkout' başlığı görünmüyor! URL: "
                        + checkoutPage.getCurrentUrl()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC11: Login olmadan checkout denemesi
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * AKIŞ:
     *  1. Giriş YAPILMADAN Harry Potter ara → ilk ürünü sepete ekle.
     *  2. "Proceed to Basket" butonuna tıkla.
     *     → AbeBooks, Amazon login formuna yönlendirir.
     *  3. ASSERT #1: id="signInSubmit" butonu görünmeli (login'e yönlendirildi).
     *  4. Login formundan giriş yap (email adımı → şifre adımı).
     *  5. Giriş sonrası AbeBooks otomatik olarak basket sayfasına döner.
     *  6. ASSERT #2: "Secure checkout" başlığı görünmeli.
     */
    @Test(description = "TC11: Login olmadan checkout — önce login formu, sonra Secure checkout görünmeli")
    public void testCheckoutWithoutLogin() {

        // 1. Giriş yapmadan arama yap ve ürünü sepete ekle
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        checkoutPage.addFirstItemToBasket();

        // 2. Proceed to Basket
        checkoutPage.proceedToBasket();

        // 3. ASSERT #1: Amazon login formu görünmeli
        Assert.assertTrue(
                checkoutPage.isRedirectedToLoginForm(),
                "TC11 FAILED [Adım 1]: Login olmadan checkout'ta Amazon login formu "
                        + "görünmeli! URL: " + checkoutPage.getCurrentUrl()
        );

        // 4. Login formundan giriş yap
        checkoutPage.loginViaAmazonForm(
                ConfigReader.getProperty("valid_email"),
                ConfigReader.getProperty("valid_password")
        );

        // 5. Login sonrası basket sayfasına dön ve Checkout butonuna bas
        checkoutPage.navigateToBasketPage();
        ReusableMethods.waitForClickability(checkoutPage.checkoutButton, 15).click();
        ReusableMethods.waitForPageLoad(10);

        // 6. ASSERT #2: "Secure checkout" başlığı görünmeli
        Assert.assertTrue(
                checkoutPage.isSecureCheckoutDisplayed(),
                "TC11 FAILED [Adım 2]: Giriş sonrası 'Secure checkout' görünmüyor! "
                        + "URL: " + checkoutPage.getCurrentUrl()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC12:
    // ─────────────────────────────────────────────────────────────────────────


}
