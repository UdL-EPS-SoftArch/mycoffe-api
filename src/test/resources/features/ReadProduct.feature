Feature: Get Product
  In order to view products
  As a user
  I want to retrieve product information


  # ========== ATRIBUTO: id ==========

  Scenario: Get product with valid ID
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name  | price |
      | Apple | 1.80  |
    When I request the product with id "1"
    Then The response code is 200

  Scenario: Get product with ID that does not exist
    Given I login as "demo" with password "password"
    When I request the product with id "999"
    Then The response code is 404


  # ========== ATRIBUTO: name ==========

  Scenario: Get product with valid name
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name   | price |
      | Orange | 2.50  |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with name "Orange"

  Scenario: Search product with name that does not exist
    Given I login as "demo" with password "password"
    When I search for products with name "NonExistentProduct"
    Then The response code is 404


  # ========== ATRIBUTO: price ==========

  Scenario: Get product with valid price
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name   | price |
      | Banana | 0.90  |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with price "0.90"

  Scenario: Get product with high price value
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name         | price   |
      | Premium Item | 9999.99 |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with price "9999.99"


  # ========== ATRIBUTO: stock ==========

  Scenario: Get product with positive stock
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name  | price | stock |
      | Bread | 1.50  | 50    |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with stock "50"

  Scenario: Get product with zero stock
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name   | price | stock |
      | Cheese | 3.50  | 0     |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with stock "0"


  # ========== ATRIBUTO: isAvailable ==========

  Scenario: Get available product
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name  | price | isAvailable |
      | Candy | 0.50  | true        |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with availability "true"

  Scenario: Get unavailable product
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name         | price | isAvailable |
      | Out of Stock | 2.00  | false       |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with availability "false"


  # ========== ATRIBUTO: rating ==========

  Scenario: Get product with maximum rating
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name      | price | rating |
      | Best Item | 10.00 | 5.0    |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with rating "5.0"

  Scenario: Get product with minimum rating
    Given I login as "demo" with password "password"
    And A product exists with the following details:
      | name       | price | rating |
      | Worst Item | 1.00  | 0.0    |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with rating "0.0"


  # ========== GET ALL PRODUCTS ==========

  Scenario: Get all products successfully
    Given I login as "demo" with password "password"
    And The following products exist:
      | name   | price |
      | Orange | 2.50  |
      | Apple  | 1.80  |
      | Banana | 0.90  |
    When I request all products
    Then The response code is 200
    And The response contains 3 products

  Scenario: Get all products when database is empty
    Given I login as "demo" with password "password"
    And No products exist
    When I request all products
    Then The response code is 200
    And The response contains 0 products


  # ========== SIN AUTENTICACIÓN ==========

  Scenario: Get product by ID when not authenticated
    Given I'm not logged in
    And A product exists with the following details:
      | name   | price |
      | Orange | 2.50  |
    When I request the product with id "1"
    Then The response code is 200
    And The response contains a product with name "Orange"

  Scenario: Get all products when not authenticated
    Given I'm not logged in
    And The following products exist:
      | name   | price |
      | Orange | 2.50  |
      | Apple  | 1.80  |
    When I request all products
    Then The response code is 200
    And The response contains 2 products