Feature: Register Product
  In order to manage my products
  As a user
  I want to register new products


  Scenario: Register a new product successfully
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name   | Orange |
      | price | 2.50  |
    Then The response code is 201


  Scenario: Register a new product that already exists
    Given I login as "demo" with password "password"
    And The product with name "Orange" is registered
    When I register a new product with the following details:
      | name   | Orange |
    Then The response code is 409


  Scenario: Register a product when not authenticated
    Given I'm not logged in
    When I register a new product with the following details:
      | name   | Apple |
    Then The response code is 401
    And The product with name "Apple" is not registered


  Scenario: Register a product with empty name
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  |  |

    Then The response code is 400
    And The product with name "" is not registered

  Scenario: Register a product with a correct price
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | Water |
      | price | 12    |
    Then The response code is 201


  Scenario: Register a product with negative price
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | Water |
      | price | -5     |
    Then The response code is 400

  Scenario: Register a product with a correct description
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | Water |
      | description | A product sample    |
    Then The response code is 201

  Scenario: Register a product with a incorrect description
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | Water |
      | description | Aproductsamplendinasidiasjdijsdijasidjsaidjasjdiasjdiajsdijsisajidojasidjaisdjaisjdisjdasijdodjisjdisajdoajsd   |
    Then The response code is 400

  Scenario: Register a product with negative stock
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | Water |
      | stock | -10   |
    Then The response code is 400

  Scenario: Register a product with correct stock
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | Water |
      | stock | 15    |
    Then The response code is 201

  Scenario: Register a product with zero stock
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | Water |
      | stock | 0     |
    Then The response code is 201

  Scenario: Register a product with a valid barcode
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name    | Water |
      | barcode | 1234567890123 |
    Then The response code is 201

  Scenario: Rgeister a product with a invalid barcode
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name    | Water |
      | barcode | 12345 |
    Then The response code is 400

  Scenario: Register a product with a negative tax
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name | Water |
      | tax  | -5    |
    Then The response code is 400

  Scenario: Register a product with a valid tax
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name | Water |
      | tax  | 15    |
    Then The response code is 201

  Scenario: Register a product with valid rating
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name   | Water |
      | rating | 4.5   |
    Then The response code is 201

  Scenario: Register a product with invalid rating
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
        | name   | Water |
        | rating | 6.0   |
    Then The response code is 400

  Scenario: Register a product with nutrition information
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name     | Yogurt |
      | kcal     | 100    |
      | carbs    | 10     |
      | proteins | 8      |
      | fats     | 2      |
    Then The response code is 201

  Scenario: Register a product with negative nutritional values
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name     | Yogurt |
      | kcal     | -10    |
    Then The response code is 400


