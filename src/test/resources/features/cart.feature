@cart
Feature: Shopping cart
  As a logged-in user
  I want to add products to my cart
  So that I can purchase them later

  Background:
    Given the user is logged in with valid credentials

  @smoke
  Scenario: Add a single product to the cart
    When the user adds "Sauce Labs Backpack" to the cart
    Then the cart badge should show 1

  @regression
  Scenario: Added products are listed on the cart page
    When the user adds "Sauce Labs Backpack" to the cart
    And the user adds "Sauce Labs Bike Light" to the cart
    And the user opens the cart
    Then the cart should contain "Sauce Labs Backpack"
    And the cart should contain "Sauce Labs Bike Light"
