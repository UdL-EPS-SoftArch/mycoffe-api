Feature: Order Management
  In order to manage customer orders
  As an authenticated user
  I want to create and consult my orders

  Background:
    Given There is a registered user with username "user1" and password "pass123" and email "user1@mycoffee.app"
    And There is a registered admin with username "admin1" and password "admin123" and email "admin1@mycoffee.app"
    And A product exists with the following details:
      | name    | Espresso |
      | price   | 1.50     |
    And A product exists with the following details:
      | name    | Cappuccino |
      | price   | 2.20       |

  # 1. Create an order successfully
  Scenario: User creates a new order successfully
    Given I login as "user1" with password "pass123"
    When I create an order with:
      | product    | quantity |
      | Espresso   | 2        |
    Then The response code is 201
    And The order should exist and include the product "Espresso"

  # 2. Error when creating an order without authentication
  Scenario: Create order without authentication
    Given I am not authenticated
    When I attempt to create an order with:
      | product    | quantity |
      | Espresso   | 1        |
    Then The response code is 401

  # 3. Retrieve an existing order
  Scenario: Retrieve an existing order
    Given I login as "user1" with password "pass123"
    And an order exists with id 1 for user "user1"
    When I retrieve the order with id 1
    Then The response code is 200
    And the response should contain the order details

  # 4. Retrieve a non-existing order
  Scenario: Retrieve a non-existing order
    Given I login as "user1" with password "pass123"
    When I retrieve the order with id 999
    Then The response code is 404

  # 5. List all orders for the user
  Scenario: List all orders for a user
    Given I login as "user1" with password "pass123"
    And the following orders exist for user "user1":
      | id | product    | quantity |
      | 1  | Cappuccino | 1        |
      | 2  | Espresso   | 2        |
    When I request my list of orders
    Then The response code is 200
    And the response should contain 2 orders

  # 6. User trying to view another user's order
  Scenario: User tries to view another user's order
    Given I login as "user1" with password "pass123"
    And an order exists with id 3 for user "admin1"
    When I retrieve the order with id 3
    Then The response code is 403
