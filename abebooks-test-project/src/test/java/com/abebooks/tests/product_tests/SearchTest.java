package com.abebooks.tests.product_tests;

import com.abebooks.base.TestBase;
import com.abebooks.pages.product.ProductDetailsPage;
import com.abebooks.pages.product.SearchPage;
import com.abebooks.utilities.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * MODÜL 2: Product Catalog Test Sınıfı
 * TC04 – Search Product
 * TC05 – Product Details View
 * TC06 – Category Filtering
 */
public class SearchTest extends TestBase {

    SearchPage searchPage;
    ProductDetailsPage detailsPage;

    @BeforeMethod
    public void setUpPages() {
        // TestBase setUp() zaten driver'ı başlatıp base_url'e gidiyor
        searchPage = new SearchPage();
        detailsPage = new ProductDetailsPage();
    }

    /**
     * TC04: Geçerli kitap adıyla arama yapıldığında sonuç gelmeli
     */
    @Test(description = "TC04: Gecerli kitap adiyla arama")
    public void testSearchValidProduct() {
        String keyword = ConfigReader.getProperty("search_keyword");

        searchPage.searchForBook(keyword);

        Assert.assertTrue(searchPage.hasSearchResults(),
                "TC04 FAIL: '" + keyword + "' aramasi icin sonuc gelmeli!");

        Assert.assertTrue(searchPage.getResultCount() > 0,
                "TC04 FAIL: En az 1 sonuc listelenmeli!");

        System.out.println("TC04 PASS: " + searchPage.getResultCount() + " sonuc bulundu.");
    }

    /**
     * TC05: Ürüne tıklandığında detay sayfasında ad ve fiyat görünmeli
     */
    @Test(description = "TC05: Urun detay sayfasi kontrolu")
    public void testProductDetailsView() {
        String keyword = ConfigReader.getProperty("search_keyword");

        searchPage.searchForBook(keyword);
        searchPage.clickFirstProduct();

        Assert.assertTrue(detailsPage.isProductTitleDisplayed(),
                "TC05 FAIL: Urun adi detay sayfasinda gorunmeli!");

        Assert.assertTrue(detailsPage.isProductPriceDisplayed(),
                "TC05 FAIL: Urun fiyati detay sayfasinda gorunmeli!");

        System.out.println("TC05 PASS: Baslik=" + detailsPage.getProductTitle()
                + " | Fiyat=" + detailsPage.getProductPrice());
    }

    /**
     * TC06: Rare Books kategorisine gidildiğinde URL uyumlu olmalı
     */
    @Test(description = "TC06: Kategori filtreleme - Rare Books")
    public void testCategoryFiltering() {
        searchPage.goToRareBooksCategory();

        String currentUrl = searchPage.getCurrentUrl();

        Assert.assertTrue(
                currentUrl.contains("rarebooks") || currentUrl.contains("rare"),
                "TC06 FAIL: URL 'rarebooks' icermeli! Mevcut URL: " + currentUrl
        );

        System.out.println("TC06 PASS: Kategori URL = " + currentUrl);
    }
}