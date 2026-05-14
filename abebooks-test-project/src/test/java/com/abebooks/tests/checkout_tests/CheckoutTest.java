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
 * TC10 – Giriş yapmadan sepete ürün ekle → checkout → yanlış e-posta gir
 *   PASS kriteri: id="auth-error-message-box" görünür
 *   ("We cannot find an account with that email address")
 *
 * TC11 – Giriş yap → sepete ürün ekle → checkout → adres formunu boş gönder
 *   PASS kriteri: id="alert--r4--children" görünür
 *   ("Enter a first and last name")
 *
 * TC12 – Boş sepette checkout (giriş yapılmamış + ürün yok)
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
        searchPage = new SearchPage();
        loginPage = new LoginPage();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC10: Giriş yapmadan sepete ürün ekle → checkout → yanlış e-posta → hata mesajı
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * AKIŞ:
     * 1. Giriş YAPILMADAN Harry Potter ara → ilk ürünü sepete ekle.
     * 2. Açılan modalda "Proceed to Basket" butonuna tıkla.
     * 3. Basket sayfasındaki "Checkout" butonuna tıkla.
     * → AbeBooks, Amazon login formuna yönlendirir.
     * 4. Login formuna yanlış e-posta gir (invalid_email) → Continue'ya bas.
     * 5. ASSERT: id="auth-error-message-box" görünmeli → PASS
     * ("We cannot find an account with that email address")
     */
    @Test(description = "TC10: Giriş yapmadan checkout → yanlış e-posta → auth hata mesajı görünmeli")
    public void testCheckoutWithInvalidEmail() {

        // 1. Giriş yapmadan arama yap ve ürünü sepete ekle
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        checkoutPage.addFirstItemToBasket();

        // 2-3. Proceed to Basket → Checkout
        checkoutPage.proceedToBasket();

        // 4. Yanlış e-posta gir ve Continue'ya bas
        checkoutPage.enterInvalidEmailOnLoginForm(
                ConfigReader.getProperty("invalid_email")
        );

        // 5. ASSERT: Auth hata mesajı görünmeli
        Assert.assertTrue(
                checkoutPage.isAuthErrorMessageDisplayed(),
                "TC10 FAILED: Yanlış e-posta sonrası auth hata mesajı görünmüyor! URL: "
                        + checkoutPage.getCurrentUrl()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TC11: Giriş yap → sepete ürün ekle → checkout → adres formunu boş gönder
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * AKIŞ:
     * 1. Geçerli bilgilerle giriş yap.
     * 2. Harry Potter ara → ilk ürünü sepete ekle.
     * 3. Açılan modalda "Proceed to Basket" butonuna tıkla.
     * 4. Basket sayfasındaki "Checkout" butonuna tıkla.
     * → <h1 class="css-edkqx3">Secure checkout</h1> görünür.
     * 5. Adres formuna HİÇBİR ŞEY GIRMEDEN "Add and continue" butonuna bas.
     * (data-test-id="create-address-save")
     * 6. ASSERT: id="alert--r4--children" görünmeli → PASS
     * ("Enter a first and last name")
     */
    @Test(description = "TC11: Giriş yap → checkout → adres formu boş gönder → hata mesajı görünmeli")
    public void testCheckoutAddressFormValidation() {

        // 1. Geçerli bilgilerle giriş yap
        loginPage.loginWithValidCredentials(
                ConfigReader.getProperty("valid_email"),
                ConfigReader.getProperty("valid_password")
        );

        // 2. Arama yap ve ilk ürünü sepete ekle
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        checkoutPage.addFirstItemToBasket();

        // 3-4. Proceed to Basket → Checkout → Secure checkout sayfasına ulaş
        checkoutPage.proceedToBasket();

        // 5. Adres formuna hiçbir şey girmeden "Add and continue" butonuna bas
        checkoutPage.clickSaveAddressWithoutInput();

        // 6. ASSERT: Adres hata mesajı görünmeli
        Assert.assertTrue(
                checkoutPage.isAddressErrorMessageDisplayed(),
                "TC11 FAILED: Adres formu boş gönderilince hata mesajı görünmüyor! URL: "
                        + checkoutPage.getCurrentUrl()
        );
    }
}
    // ─────────────────────────────────────────────────────────────────────────
    // TC12: Boş sepette checkout denemesi
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * AKIŞ:
     *  1. Giriş YAPILMADAN doğrudan basket sayfasına git.
     *  2. ASSERT: "You don't have any items in your basket." görünmeli.
     */
    /**
    @Test(description = "TC12: Boş sepette checkout — boş sepet mesajı görünmeli")
    public void testEmptyCartCheckout() {

        // Giriş YAPILMADAN doğrudan basket sayfasına git
        checkoutPage.navigateToBasketPage();

        // ASSERT: "You don't have any items in your basket." mesajı görünmeli
        Assert.assertTrue(
                checkoutPage.isEmptyBasketMessageDisplayed(),
                "TC12 FAILED: Boş sepet mesajı görünmüyor! URL: "
                        + checkoutPage.getCurrentUrl()
        );
    }
}**/