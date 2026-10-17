package com.framework.context;

import com.framework.driver.DriverManager;
import com.framework.pages.CartPage;
import com.framework.pages.InventoryPage;
import com.framework.pages.LoginPage;
import org.openqa.selenium.WebDriver;

/**
 * Per-scenario state container injected by PicoContainer into hooks and step classes.
 * Page objects are created lazily so the driver (started in the @Before hook) already exists.
 */
public class TestContext {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    public WebDriver driver() {
        return DriverManager.getDriver();
    }

    public LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver());
        }
        return loginPage;
    }

    public InventoryPage inventoryPage() {
        if (inventoryPage == null) {
            inventoryPage = new InventoryPage(driver());
        }
        return inventoryPage;
    }

    public CartPage cartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(driver());
        }
        return cartPage;
    }
}
