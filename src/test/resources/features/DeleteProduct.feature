Feature: Delete Product
  In order to manage products
  As a user with appropriate permissions
  I want to delete products from the system

  Scenario: Delete product with ID that does not exist
    Given I login as "demo" with password "password"
    When I delete the product with id "999"
    Then The response code is 404

  Scenario: Delete product without authentication
    Given I'm not logged in
    When I delete the product with id "1"
    Then The response code is 401

  Scenario: Delete product with valid ID
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name  | Grapes |
      | price | 3.00   |
    When I delete the product with id "1"
    Then The response code is 200
    And The product with name "Grapes" is not registered