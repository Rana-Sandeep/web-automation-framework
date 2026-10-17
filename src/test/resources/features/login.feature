@login
Feature: Login
  As a registered user
  I want to log in to the application
  So that I can access the product catalogue

  Background:
    Given the user is on the login page

  @smoke
  Scenario: Successful login with valid credentials
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the products page should be displayed

  @regression @negative
  Scenario Outline: Login is rejected for invalid input
    When the user logs in with username "<username>" and password "<password>"
    Then the error message "<message>" should be displayed

    Examples:
      | username        | password     | message                                                                   |
      | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.                       |
      | standard_user   | wrong_pass   | Epic sadface: Username and password do not match any user in this service |
      |                 | secret_sauce | Epic sadface: Username is required                                        |
