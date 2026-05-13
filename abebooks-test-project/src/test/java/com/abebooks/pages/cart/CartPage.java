package com.abebooks.pages.cart;

import com.abebooks.utilities.Driver;
import com.abebooks.utilities.ReusableMethods;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * MODÜL 3: Shopping Cart (Alışveriş Sepeti)
 * Sorumlu: Merve
 * Test Case'ler: TC07, TC08, TC09
 */
public class CartPage {

    public CartPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    // ===== LOCATOR'LAR =====

    @FindBy(css = ".add-to-basket, button[class*='add-to-basket'], a[class*='add-to-cart']")
    public WebElement addToCartButton;

    @FindBy(css = "a[href*='ShopBasket'], .basket-link, #basket-link")
    public WebElement basketLink;

    @FindBy(css = ".basket-item, .cart-item, [class*='basket-row']")
    public List<WebElement> cartItems;

    @FindBy(css = "button[class*='remove'], a[class*='remove'], .delete-item")
    public List<WebElement> removeButtons;

    @FindBy(css = "input[name='quantity'], input[class*='quantity'], .qty-input")
    public WebElement quantityInput;

    @FindBy(css = "button[class*='update'], .update-qty, input[value='Update']")
    public WebElement updateQuantityButton;

    @FindBy(css = ".basket-total, .cart-total, [class*='total-price']")
    public WebElement cartTotal;

    @FindBy(css = ".empty-basket, .empty-cart, [class*='empty']")
    public WebElement emptyCartMessage;

    // ===== METOTLAR =====

    /**
     * TC07 - Detay sayfasındaki ürünü sepete ekle
     */
    public void addCurrentProductToCart() {
        ReusableMethods.waitForClickability(addToCartButton, 10);
        ReusableMethods.jsClick(addToCartButton);
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC07 - Sepet sayfasına git
     */
    public void goToCart() {
        ReusableMethods.waitForClickability(basketLink, 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC07 - Sepette ürün var mı kontrol et
     */
    public boolean isCartNotEmpty() {
        try {
            ReusableMethods.waitForVisibility(cartItems.get(0), 10);
            return !cartItems.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * TC07 - Sepetteki ürün sayısını döndür
     */
    public int getCartItemCount() {
        return cartItems.size();
    }

    /**
     * TC08 - İlk ürünü sepetten kaldır
     */
    public void removeFirstItem() {
        ReusableMethods.waitForClickability(removeButtons.get(0), 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC08 - Sepet boş mu kontrol et
     */
    public boolean isCartEmpty() {
        try {
            ReusableMethods.waitForVisibility(emptyCartMessage, 5);
            return emptyCartMessage.isDisplayed();
        } catch (Exception e) {
            return cartItems.isEmpty();
        }
    }

    /**
     * TC09 - Ürün miktarını güncelle
     */
    public void updateQuantity(String quantity) {
        ReusableMethods.waitForVisibility(quantityInput, 10).clear();
        quantityInput.sendKeys(quantity);
        ReusableMethods.waitForClickability(updateQuantityButton, 10).click();
        ReusableMethods.waitForPageLoad(10);
    }

    /**
     * TC09 - Mevcut miktarı oku
     */
    public String getCurrentQuantity() {
        ReusableMethods.waitForVisibility(quantityInput, 10);
        return quantityInput.getAttribute("value");
    }
}
