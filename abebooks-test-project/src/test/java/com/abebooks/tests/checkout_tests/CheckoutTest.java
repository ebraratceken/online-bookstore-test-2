package com.abebooks.tests.checkout_tests;

import com.abebooks.base.TestBase;
import com.abebooks.pages.auth.LoginPage;
import com.abebooks.pages.cart.CartPage;
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
 * TC10 – Checkout Flow (Valid)
 * TC11 – Checkout Without Login (AbeBooks'ta login ZORUNLU → login sayfasına yönlendirilmeli)
 * TC12 – Empty Cart Checkout
 */
public class CheckoutTest extends TestBase {

    CheckoutPage checkoutPage;
    CartPage cartPage;
    SearchPage searchPage;
    LoginPage loginPage;

    @BeforeMethod
    public void setUpPage() {
        checkoutPage = new CheckoutPage();
        cartPage = new CartPage();
        searchPage = new SearchPage();
        loginPage = new LoginPage();
    }

    /**
     * TC10: Giriş yapılmış + sepette ürün varken checkout başlatılabilmeli
     * Expected: Checkout süreci başarıyla başlamalı
     */
    @Test(description = "TC10: Geçerli checkout akışı")
    public void testCheckoutFlowValid() {
        // Giriş yap
        loginPage.loginWithValidCredentials(
            ConfigReader.getProperty("valid_email"),
            ConfigReader.getProperty("valid_password")
        );

        // Sepete ürün ekle
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        searchPage.clickFirstProduct();
        cartPage.addCurrentProductToCart();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isCartNotEmpty(), "Test ön koşulu: Sepette ürün olmalı!");

        // Checkout başlat
        checkoutPage.clickCheckout();

        Assert.assertTrue(checkoutPage.isOnCheckoutPage(),
            "Checkout butonuna tıklandıktan sonra checkout sayfasına geçilmeli! URL: "
            + checkoutPage.getCurrentUrl());
    }

    /**
     * TC11: Login olmadan checkout denendiğinde login sayfasına yönlendirilmeli
     *
     * NOT: AbeBooks'ta BKM Kitap'tan farklı olarak üye olmadan alışveriş YAPILAMAZ.
     * Bu nedenle Expected davranış: Login sayfasına yönlendirme / hata mesajı.
     *
     * Expected: Login sayfasına yönlendirilmeli
     */
    @Test(description = "TC11: Login olmadan checkout denemesi")
    public void testCheckoutWithoutLogin() {
        // Login YAPILMADAN doğrudan sepet sayfasına git
        driver.get("https://www.abebooks.com/servlet/ShopBasketPL");
        ReusableMethods waitHelper = null; // Sayfanın yüklenmesi için

        // Checkout butonunu bulmaya çalış
        try {
            checkoutPage.clickCheckout();
        } catch (Exception e) {
            // Buton bulunamadıysa zaten yönlendirilmiş olabilir
        }

        // AbeBooks login'i zorunlu kılar → login sayfasına yönlendirmeli
        boolean redirectedToLogin = checkoutPage.isRedirectedToLogin();
        boolean onCheckoutPage = checkoutPage.isOnCheckoutPage();

        Assert.assertTrue(redirectedToLogin && !onCheckoutPage,
            "Login olmadan checkout yapılamamalı! Kullanıcı login sayfasına yönlendirilmeli. "
            + "Mevcut URL: " + checkoutPage.getCurrentUrl());
    }

    /**
     * TC12: Boş sepette checkout denendiğinde uyarı mesajı gösterilmeli
     * Expected: Uyarı mesajı veya checkout butonu devre dışı
     */
    @Test(description = "TC12: Boş sepette checkout denemesi")
    public void testEmptyCartCheckout() {
        // Giriş yap
        loginPage.loginWithValidCredentials(
            ConfigReader.getProperty("valid_email"),
            ConfigReader.getProperty("valid_password")
        );

        // Doğrudan boş sepet sayfasına git
        driver.get("https://www.abebooks.com/servlet/ShopBasketPL");

        // Checkout butonuna basmayı dene
        try {
            checkoutPage.clickCheckout();
        } catch (Exception e) {
            // Boş sepette buton olmayabilir — bu da expected bir durumdur
        }

        boolean warningShown = checkoutPage.isEmptyCartWarningDisplayed();
        boolean cartIsEmpty = cartPage.isCartEmpty();

        Assert.assertTrue(warningShown || cartIsEmpty,
            "Boş sepette checkout denendiğinde uyarı mesajı gösterilmeli veya sepet boş olduğu belirtilmeli!");
    }
}
