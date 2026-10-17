package com.framework.stepdefinitions;

import com.framework.context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        context.loginPage().open();
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWith(String username, String password) {
        context.loginPage().login(username, password);
    }

    @Then("the products page should be displayed")
    public void theProductsPageShouldBeDisplayed() {
        Assert.assertTrue(context.inventoryPage().isLoaded(), "Products page was not displayed after login");
    }

    @Then("the error message {string} should be displayed")
    public void theErrorMessageShouldBeDisplayed(String expected) {
        Assert.assertEquals(context.loginPage().getErrorMessage(), expected, "Unexpected login error message");
    }
}
