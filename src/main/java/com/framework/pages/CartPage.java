package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private static final By CART_ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    private static final By PAGE_TITLE = By.cssSelector(".title");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(PAGE_TITLE) && getText(PAGE_TITLE).equals("Your Cart");
    }

    public List<String> getItemNames() {
        return findAll(CART_ITEM_NAMES).stream().map(WebElement::getText).map(String::trim).toList();
    }
}
