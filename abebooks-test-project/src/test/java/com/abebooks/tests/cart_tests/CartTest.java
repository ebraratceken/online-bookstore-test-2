package com.abebooks.tests.cart_tests;

import com.abebooks.pages.cart.CartPage;
import com.abebooks.utilities.ConfigReader;
import com.abebooks.utilities.Driver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * MODÜL 3: Shopping Cart Tests
 * Test Case'ler: TC07, TC08, TC09
 */
public class CartTest {

    CartPage cartPage;

    @BeforeMethod
    public void setUp() {
        cartPage = new CartPage();
        cartPage.login(
                ConfigReader.getProperty("valid_email"),
                ConfigReader.getProperty("valid_password")
        );
    }

    @AfterMethod
    public void tearDown() {
        Driver.quitDriver();
    }

    /**
     * TC07 - Sepete Ürün Ekleme
     * Kullanıcı ürünü sepete ekleyebilmeli.
     * Sepet boş olmamalı ve en az 1 ürün içermeli.
     */
    @Test(description = "TC07 - Add product to shopping cart")
    public void tc07_addToCart() {

        cartPage.searchForBook(ConfigReader.getProperty("search_keyword"));

        Assert.assertTrue(
                cartPage.hasSearchResults(),
                "TC07 FAIL: Arama sonucu gelmedi."
        );

        cartPage.addCurrentProductToCart();

        Assert.assertTrue(
                cartPage.isCartNotEmpty(),
                "TC07 FAIL: Sepet boş olmamalı!"
        );

        Assert.assertTrue(
                cartPage.getCartItemCount() >= 1,
                "TC07 FAIL: Sepette en az 1 ürün olmalı!"
        );

        System.out.println("TC07 PASS: Sepetteki ürün sayısı = " + cartPage.getCartItemCount());
    }

    /**
     * TC08 - Sepetten Ürün Kaldırma
     * Kullanıcı sepetteki ürünü silebilmeli.
     * Kaldırma sonrası ürün sayısı azalmalı veya sepet boşalmalı.
     */
    @Test(description = "TC08 - Remove product from shopping cart")
    public void tc08_removeFromCart() {

        cartPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        cartPage.addCurrentProductToCart();
        cartPage.goToCart();

        Assert.assertTrue(
                cartPage.isCartNotEmpty(),
                "TC08 FAIL: Ön koşul başarısız — sepette ürün olmalı!"
        );

        int countBefore = cartPage.getCartItemCount();
        cartPage.removeFirstItem();
        int countAfter = cartPage.getCartItemCount();

        Assert.assertTrue(
                cartPage.isCartEmpty() || countAfter < countBefore,
                "TC08 FAIL: Ürün sayısı azalmalıydı! Önce: "
                        + countBefore + " Sonra: " + countAfter
        );

        System.out.println("TC08 PASS: Önce = " + countBefore + " | Sonra = " + countAfter);
    }

    /**
     * TC09 - Ürün Adedi Güncelleme
     * Kullanıcı sepetteki ürünün adedini güncelleyebilmeli.
     * Güncelleme sonrası adet "2" olarak görünmeli.
     */
    @Test(description = "TC09 - Update item quantity in shopping cart")
    public void tc09_updateQuantity() {

        cartPage.searchForBook(ConfigReader.getProperty("search_keyword"));
        cartPage.addCurrentProductToCart();
        cartPage.goToCart();

        Assert.assertTrue(
                cartPage.isCartNotEmpty(),
                "TC09 FAIL: Ön koşul başarısız — sepette ürün olmalı!"
        );

        cartPage.updateQuantity("2");

        String qty = cartPage.getCurrentQuantity();

        Assert.assertEquals(
                qty,
                "2",
                "TC09 FAIL: Beklenen adet 2, gerçekleşen: " + qty
        );

        System.out.println("TC09 PASS: Adet = " + qty);
    }
}
