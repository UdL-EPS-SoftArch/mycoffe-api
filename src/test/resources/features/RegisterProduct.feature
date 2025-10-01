Feature: Register Product
  In order to manage my products
  As a user
  I want to register new products

  Scenario: Register a new product successfully
    Given I'm logged in as "seller" with password "password"
    And There is no product with name "name"
    When I register a new product with name "name"