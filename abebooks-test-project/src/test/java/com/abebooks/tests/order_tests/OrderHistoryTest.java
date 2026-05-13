package com.abebooks.tests.order_tests;

import com.abebooks.pages.orders.OrderHistoryPage;
import com.abebooks.utilities.ConfigReader;
import com.abebooks.utilities.Driver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OrderHistoryTest {

    OrderHistoryPage orderHistoryPage;

    @BeforeMethod
    public void setUp() {
        orderHistoryPage = new OrderHistoryPage();
        orderHistoryPage.login(
                ConfigReader.getProperty("valid_email"),
                ConfigReader.getProperty("valid_password")
        );
    }

    @AfterMethod
    public void tearDown() {
        Driver.quitDriver();
    }

    /**
     * TC13 - Access My Purchases Page
     * Kullanici My Purchases sayfasini basariyla acabilmeli.
     * Siparis olup olmamasi onemsiz — sayfa yuklendiyse PASS.
     */
    @Test(description = "TC13 - Access My Purchases Page")
    public void tc13_accessMyPurchasesPage() {

        orderHistoryPage.goToOrderHistory();

        Assert.assertTrue(
                orderHistoryPage.isOrdersPageLoaded(),
                "TC13 FAIL: My Purchases sayfasi yuklenemedi."
        );

        System.out.println("TC13 PASS: My Purchases sayfasi basariyla yuklendi.");
        System.out.println("URL: " + orderHistoryPage.getCurrentUrl());
    }

    /**
     * TC14 - Verify Empty Order Message
     * Gecersiz siparis no ile arama yapilir, no results mesaji beklenir.
     */
    @Test(description = "TC14 - Verify Empty Order Message")
    public void tc14_verifyEmptyOrderMessage() {

        orderHistoryPage.goToOrderHistory();

        Assert.assertTrue(
                orderHistoryPage.isOrdersPageLoaded(),
                "TC14 FAIL: My Purchases sayfasi yuklenemedi."
        );

        orderHistoryPage.searchByOrderNumber("0000000000");

        Assert.assertTrue(
                orderHistoryPage.isNoResultsMessageDisplayed(),
                "TC14 FAIL: 'No results were found matching your search criteria.' mesaji goruntulenемedi."
        );

        System.out.println("TC14 PASS: Siparis bulunamadi mesaji dogrulandi.");
    }

    /**
     * TC15 - Search with Invalid Order Number
     * Gecersiz siparis numarasi girildiginde sistem cokmemeli, uygun mesaj gostermeli.
     */
    @Test(description = "TC15 - Search with Invalid Order Number")
    public void tc15_searchWithInvalidOrderNumber() {

        orderHistoryPage.goToOrderHistory();

        Assert.assertTrue(
                orderHistoryPage.isOrdersPageLoaded(),
                "TC15 FAIL: My Purchases sayfasi yuklenemedi."
        );

        orderHistoryPage.searchByOrderNumber("9999999999");

        Assert.assertTrue(
                orderHistoryPage.isNoResultsMessageDisplayed(),
                "TC15 FAIL: Gecersiz siparis numarasi icin uygun mesaj gosterilmedi."
        );

        System.out.println("TC15 PASS: Gecersiz siparis numarasi duzgun handle edildi.");
    }
}