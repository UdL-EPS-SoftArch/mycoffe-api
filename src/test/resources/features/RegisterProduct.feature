Feature: Register Product
  In order to manage my products
  As a user
  I want to register new products


  Scenario: Register a new product successfully
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name   | price |
      | Orange | 2.50  |
    Then The response code is 201


  Scenario: Register a new product that already exists
    Given I login as "demo" with password "password"
    And The product with name "Orange" is registered
    When I register a new product with the following details:
      | name   |
      | Orange |
    Then The response code is 400


  Scenario: Register a product when not authenticated
    Given I'm not logged in
    When I register a new product with the following details:
      | name  |
      | Apple |
    Then The response code is 401
    And The product with name "Apple" is not registered


  Scenario: Register a product with empty name
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  |
      |  |
    Then The response code is 400
    And The product with name "" is not registered

  Scenario: Register a product with a correct price
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | price |
      | Water | 12    |
    Then The response code is 201


  Scenario: Register a product with negative price
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | price |
      | Water | -5     |
    Then The response code is 400

  Scenario: Register a product with a correct description
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | description |
      | Water | A product sample    |
    Then The response code is 201

  Scenario: Register a product with a incorrect description
    Given I login as "demo" with password "password"
    When I register a new product with the following details:
      | name  | description |
      | Water | Aproductsamplendinasidiasjdijsdijasidjsaidjasjdiasjdiajsdijsisajidojasidjaisdjaisjdisjdasijdodjisjdisajdoajsd   |
    Then The response code is 400



  ## TODO check user roles
  ## check db initialization records
  ## check if ...
