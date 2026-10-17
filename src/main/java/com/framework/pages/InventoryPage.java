package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class InventoryPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(PAGE_TITLE) && getText(PAGE_TITLE).equals("Products");
    }

    /** SauceDemo button ids follow the pattern add-to-cart-<product-name-with-dashes>. */
    public void addToCart(String productName) {
        String id = "add-to-cart-" + productName.toLowerCase().replace(' ', '-');
        log.info("Adding '{}' to cart", productName);
        click(By.id(id));
    }

    public int getCartCount() {
        List<?> badge = findAll(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(getText(CART_BADGE));
    }

    public void openCart() {
        click(CART_LINK);
    }
}
