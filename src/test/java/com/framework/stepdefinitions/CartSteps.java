package com.framework.stepdefinitions;

import com.framework.config.ConfigReader;
import com.framework.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CartSteps {

    private final TestContext context;

    public CartSteps(TestContext context) {
        this.context = context;
    }

    @Given("the user is logged in with valid credentials")
    public void theUserIsLoggedIn() {
        context.loginPage().open().login(ConfigReader.get("valid.username"), ConfigReader.get("valid.password"));
        Assert.assertTrue(context.inventoryPage().isLoaded(), "Login precondition failed");
    }

    @When("the user adds {string} to the cart")
    public void theUserAddsToTheCart(String product) {
        context.inventoryPage().addToCart(product);
    }

    @When("the user opens the cart")
    public void theUserOpensTheCart() {
        context.inventoryPage().openCart();
    }

    @Then("the cart badge should show {int}")
    public void theCartBadgeShouldShow(int count) {
        Assert.assertEquals(context.inventoryPage().getCartCount(), count, "Cart badge count mismatch");
    }

    @Then("the cart should contain {string}")
    public void theCartShouldContain(String product) {
        Assert.assertTrue(context.cartPage().isLoaded(), "Cart page was not displayed");
        Assert.assertTrue(context.cartPage().getItemNames().contains(product),
                "Cart does not contain '" + product + "'. Actual: " + context.cartPage().getItemNames());
    }
}
