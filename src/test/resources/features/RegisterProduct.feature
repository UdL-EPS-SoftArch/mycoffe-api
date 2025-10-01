Feature: Register Product
  In order to manage my products
  As a user
  I want to register new products

  Scenario: Register a new product successfully
    Given I'm logged in as "seller" with password "password"
    And There is no product with name "capucchino"
    And There is a category with name "coffee"
    When I register a new product with name "capucchino", description "-", price "3", stock "10", kcal "100",
    Then The response code is 201