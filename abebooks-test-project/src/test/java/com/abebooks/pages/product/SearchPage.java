package com.abebooks.pages.product;

import com.abebooks.utilities.Driver;
import com.abebooks.utilities.ReusableMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * MODÜL 2: Product Catalog
 * TC04, TC05, TC06
 */
public class SearchPage {

    private org.openqa.selenium.WebDriver driver = Driver.getDriver();

    public SearchPage() {
        PageFactory.initElements(driver, this);
    }

    // Arama kutusu - name="kn"
    @FindBy(name = "kn")
    public List<WebElement> searchInputs;

    // Arama butonu
    @FindBy(css = "button[type='submit']")
    public List<WebElement> searchButtons;

    // Arama sonuçları
    @FindBy(css = ".result-set .result, .listing-container, [class*='srp-item']")
    public List<WebElement> searchResults;

    // Kitap başlıkları - gerçek data-test-id
    @FindBy(css = "[data-test-id='listing-title']")
    public List<WebElement> bookTitles;

    // Kategori linkleri
    @FindBy(css = "a[href='https://www.abebooks.com/books/rarebooks/']")
    public WebElement rareBooksCategory;

    @FindBy(css = "a[href='https://www.abebooks.com/books/used-books.shtml']")
    public WebElement usedBooksCategory;

    // ===== METOTLAR =====

    public void goToHomePage() {
        driver.get("https://www.abebooks.com");
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC04 - Görünür arama kutusunu bulup arama yap
     */
    public void searchForBook(String keyword) {
        WebElement visibleInput = null;
        for (WebElement input : driver.findElements(By.name("kn"))) {
            if (input.isDisplayed()) {
                visibleInput = input;
                break;
            }
        }
        if (visibleInput == null) {
            visibleInput = driver.findElement(By.name("kn"));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.display='block'; arguments[0].style.visibility='visible';",
                    visibleInput
            );
        }
        visibleInput.clear();
        visibleInput.sendKeys(keyword);

        for (WebElement btn : driver.findElements(By.cssSelector("button[type='submit']"))) {
            if (btn.isDisplayed()) {
                btn.click();
                break;
            }
        }
        ReusableMethods.waitForPageLoad(15);
    }

    /**
     * TC04 - Arama sonucu var mı?
     */
    public boolean hasSearchResults() {
        try {
            ReusableMethods.waitForVisibility(searchResults.get(0), 10);
            return !searchResults.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public int getResultCount() {
        return searchResults.size();
    }

    /**
     * TC05 - İlk ürüne tıkla
     * data-test-id="listing-title" bir <span>, closest('a') ile parent linke tıkla
     */
    public void clickFirstProduct() {
        ReusableMethods.waitForVisibility(bookTitles.get(0), 10);
        WebElement titleSpan = bookTitles.get(0);

        // Span'ın en yakın <a> atası
        WebElement parentLink = (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].closest('a');", titleSpan);

        if (parentLink != null) {
            parentLink.click();
        } else {
            titleSpan.click();
        }
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC06 - Rare Books kategorisine git
     */
    public void goToRareBooksCategory() {
        ReusableMethods.waitForClickability(rareBooksCategory, 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    public void goToUsedBooksCategory() {
        ReusableMethods.waitForClickability(usedBooksCategory, 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}