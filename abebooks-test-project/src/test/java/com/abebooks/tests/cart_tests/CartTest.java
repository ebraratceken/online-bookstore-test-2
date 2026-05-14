package com.abebooks.tests.cart_tests;

import com.abebooks.base.BaseTest;
import com.abebooks.pages.auth.LoginPage;
import com.abebooks.pages.cart.CartPage;
import com.abebooks.pages.product.SearchPage;
import com.abebooks.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * MODÜL 3: Shopping Cart Test Sınıfı
 * Sorumlu: Merve
 *
 * TC07 – Add to Cart
 * TC08 – Remove from Cart
 * TC09 – Update Quantity
 */
public class CartTest extends BaseTest {

    CartPage cartPage;
    SearchPage searchPage;
    LoginPage loginPage;

    @BeforeMethod
    public void setUpPage() {
        cartPage = new CartPage();
        searchPage = new SearchPage();
        loginPage = new LoginPage();

        // Her test öncesi giriş yap
        loginPage.loginWithValidCredentials(
            ConfigReader.getProperty("valid_email"),
            ConfigReader.getProperty("valid_password")
        );
    }

    /**
     * TC07: Ürün sepete başarıyla eklenebilmeli
     * Expected: Ürün sepete eklendi mesajı veya sepet sayacı arttı
     */
    @Test(description = "TC07: Ürün sepete ekle")
    public void testAddToCart() {
        // Ürünü bul ve detay sayfasına git
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        searchPage.clickFirstProduct();

        // Sepete ekle
        cartPage.addCurrentProductToCart();

        // Sepete git ve kontrol et
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isCartNotEmpty(),
            "Ürün sepete eklendikten sonra sepet boş olmamalı!");

        Assert.assertTrue(cartPage.getCartItemCount() >= 1,
            "Sepette en az 1 ürün bulunmalı!");
    }

    /**
     * TC08: Sepetten ürün kaldırılabilmeli
     * Expected: Ürün sepetten silindi
     */
    @Test(description = "TC08: Sepetten ürün kaldır")
    public void testRemoveFromCart() {
        // Önce sepete ürün ekle
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        searchPage.clickFirstProduct();
        cartPage.addCurrentProductToCart();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isCartNotEmpty(), "Test ön koşulu: Sepette ürün olmalı!");

        int beforeCount = cartPage.getCartItemCount();

        // Ürünü kaldır
        cartPage.removeFirstItem();

        int afterCount = cartPage.getCartItemCount();

        Assert.assertTrue(afterCount < beforeCount || cartPage.isCartEmpty(),
            "Ürün kaldırıldıktan sonra sepetteki ürün sayısı azalmalı!");
    }

    /**
     * TC09: Sepetteki ürün miktarı güncellenebilmeli
     * Expected: Miktar doğru şekilde güncellendi
     */
    @Test(description = "TC09: Sepet miktarı güncelle")
    public void testUpdateQuantity() {
        // Önce sepete ürün ekle
        searchPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        searchPage.clickFirstProduct();
        cartPage.addCurrentProductToCart();
        cartPage.goToCart();

        Assert.assertTrue(cartPage.isCartNotEmpty(), "Test ön koşulu: Sepette ürün olmalı!");

        // Miktarı 2 yap
        cartPage.updateQuantity("2");

        String updatedQuantity = cartPage.getCurrentQuantity();

        Assert.assertEquals(updatedQuantity, "2",
            "Miktar 2 olarak güncellenmeli! Mevcut değer: " + updatedQuantity);
    }
}
