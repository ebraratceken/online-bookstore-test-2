package com.abebooks.pages.cart;

import com.abebooks.utilities.Driver;
import com.abebooks.utilities.ReusableMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * MODÜL 3: Shopping Cart (Alışveriş Sepeti)
 * Test Case'ler: TC07, TC08, TC09
 */
public class CartPage {

    private org.openqa.selenium.WebDriver driver = Driver.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

    public CartPage() {
        PageFactory.initElements(driver, this);
    }

    // ===== CSS SELECTOR SABİTLERİ =====

    private static final By ADD_TO_BASKET = By.cssSelector("a[data-csa-c-action='add-to-basket']");
    private static final By DELETE_BUTTON  = By.cssSelector("button[data-csa-c-action='remove-item']");
    private static final By QUANTITY_INPUT = By.cssSelector("input[data-csa-c-action='quantity-update']");

    // ===== LOGIN LOCATOR'LARI =====

    @FindBy(id = "sign-on")
    public WebElement signOnLink;

    @FindBy(id = "ap_email")
    public WebElement emailField;

    @FindBy(id = "ap_password")
    public WebElement passwordField;

    @FindBy(id = "signInSubmit")
    public WebElement signInSubmitButton;

    // ===== METOTLAR =====

    /**
     * Login işlemi
     * AbeBooks: önce email → submit, sonra password → submit
     */
    public void login(String email, String password) {
        driver.get("https://www.abebooks.com");
        ReusableMethods.waitForClickability(signOnLink, 10).click();

        ReusableMethods.waitForVisibility(emailField, 10);
        emailField.clear();
        emailField.sendKeys(email);
        ReusableMethods.waitForClickability(signInSubmitButton, 10).click();

        ReusableMethods.waitForVisibility(passwordField, 10);
        passwordField.clear();
        passwordField.sendKeys(password);
        ReusableMethods.waitForClickability(signInSubmitButton, 10).click();

        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC07/TC08/TC09 - Kitap ara
     * Her çağrıda önce ana sayfaya gider — böylece name="kn" arama kutusu
     * her zaman DOM'da hazır olur (sepet sayfasında bu element yok).
     */
    public void searchForBook(String keyword) {
        driver.get("https://www.abebooks.com");
        ReusableMethods.waitForPageLoad(10);

        // Görünür arama input'unu bul
        WebElement visibleInput = null;
        for (WebElement input : driver.findElements(By.name("kn"))) {
            if (input.isDisplayed()) {
                visibleInput = input;
                break;
            }
        }
        if (visibleInput == null) {
            visibleInput = driver.findElement(By.name("kn"));
        }
        visibleInput.clear();
        visibleInput.sendKeys(keyword);

        // Görünür submit butonunu bul ve tıkla
        for (WebElement btn : driver.findElements(By.cssSelector("button[type='submit']"))) {
            if (btn.isDisplayed()) {
                btn.click();
                break;
            }
        }
        ReusableMethods.waitForPageLoad(15);
    }

    /**
     * TC07 - Arama sonucu var mı?
     */
    public boolean hasSearchResults() {
        try {
            List<WebElement> titles = driver.findElements(
                    By.cssSelector("[data-test-id='listing-title']"));
            ReusableMethods.waitForVisibility(titles.get(0), 10);
            return !titles.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC07 - Sepete ürün ekle
     * İlk "Add to basket" linkinin href'inden navigate eder.
     */
    public void addCurrentProductToCart() {
        wait.until(ExpectedConditions.presenceOfElementLocated(ADD_TO_BASKET));

        List<WebElement> buttons = driver.findElements(ADD_TO_BASKET);

        if (buttons.isEmpty()) {
            throw new RuntimeException("TC07 FAIL: Add to basket butonu bulunamadı.");
        }

        String href = buttons.get(0).getAttribute("href");

        if (href != null && href.contains("/checkout/basket")) {
            driver.get(href);
        } else {
            ReusableMethods.jsClick(buttons.get(0));
        }

        ReusableMethods.waitForPageLoad(10);

        // Delete butonunun gelmesini bekle → sepete eklendi onayı
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(DELETE_BUTTON));
            System.out.println("[TC07] Ürün sepete eklendi. URL: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("[TC07] UYARI: Delete butonu bulunamadı. URL: " + driver.getCurrentUrl());
        }
    }

    /**
     * Sepet dolu mu?
     */
    public boolean isCartNotEmpty() {
        return !isCartEmpty();
    }

    /**
     * Sepet boş mu?
     */
    public boolean isCartEmpty() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(DELETE_BUTTON));
            List<WebElement> items = driver.findElements(DELETE_BUTTON);
            return items.isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Sepetteki ürün sayısı
     */
    public int getCartItemCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(DELETE_BUTTON));
            List<WebElement> items = driver.findElements(DELETE_BUTTON);
            System.out.println("[CartPage] Sepet ürün sayısı: " + items.size());
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * TC08/TC09 - Sepet sayfasına git
     */
    public void goToCart() {
        driver.get("https://www.abebooks.com/checkout/basket");
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC08 - Sepetten ilk ürünü kaldır
     */
    public void removeFirstItem() {
        wait.until(ExpectedConditions.presenceOfElementLocated(DELETE_BUTTON));
        WebElement deleteBtn = driver.findElements(DELETE_BUTTON).get(0);
        ReusableMethods.waitForClickability(deleteBtn, 10);
        ReusableMethods.jsClick(deleteBtn);

        wait.until(ExpectedConditions.stalenessOf(deleteBtn));
        ReusableMethods.waitForPageLoad(5);
        System.out.println("[TC08] Ürün sepetten kaldırıldı.");
    }

    /**
     * TC09 - Adet güncelle
     * clear() AbeBooks'ta çalışmıyor;
     * CTRL+A ile seç → üzerine yaz → TAB ile event'i tetikle.
     */
    public void updateQuantity(String quantity) {
        wait.until(ExpectedConditions.elementToBeClickable(QUANTITY_INPUT));
        WebElement input = driver.findElement(QUANTITY_INPUT);
        input.click();
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(quantity);
        input.sendKeys(Keys.TAB);
        ReusableMethods.waitForPageLoad(5);
        System.out.println("[TC09] Adet güncellendi: " + quantity);
    }

    /**
     * TC09 - Mevcut adet değerini oku
     */
    public String getCurrentQuantity() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(QUANTITY_INPUT));
        WebElement input = driver.findElement(QUANTITY_INPUT);
        String val = input.getAttribute("value");
        System.out.println("[TC09] Mevcut adet: " + val);
        return val != null ? val.trim() : "-1";
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
