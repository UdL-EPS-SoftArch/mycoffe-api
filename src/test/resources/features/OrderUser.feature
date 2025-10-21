Feature: Order Management
  In order to manage customer orders
  As an authenticated user
  I want to create and consult my orders

  Background:
    Given the following users exist:
      | username | password | role   |
      | user1    | pass123  | USER   |
      | admin1   | admin123 | ADMIN  |

    And the following products exist:
      | name        | price |
      | Espresso    | 1.50  |
      | Cappuccino  | 2.20  |

  # 1. Create an order successfully
  Scenario: User creates a new order successfully
    Given I am authenticated as "user1" with password "pass123"
    When I create an order with:
      | product    | quantity |
      | Espresso   | 2        |
    Then the response status should be 201
    And the order should exist in the system for user "user1"

  # 2. Error when creating an order without authentication
  Scenario: Create order without authentication
    Given I am not authenticated
    When I attempt to create an order with:
      | product    | quantity |
      | Espresso   | 1        |
    Then the response status should be 401

  # 3. Retrieve an existing order
  Scenario: Retrieve an existing order
    Given I am authenticated as "user1" with password "pass123"
    And an order exists with id 1 for user "user1"
    When I retrieve the order with id 1
    Then the response status should be 200
    And the response should contain the order details

  # 4. Retrieve a non-existing order
  Scenario: Retrieve a non-existing order
    Given I am authenticated as "user1" with password "pass123"
    When I retrieve the order with id 999
    Then the response status should be 404

  # 5. List all orders for the user
  Scenario: List all orders for a user
    Given I am authenticated as "user1" with password "pass123"
    And the following orders exist for user "user1":
      | id | product    | quantity |
      | 1  | Cappuccino | 1        |
      | 2  | Espresso   | 2        |
    When I request my list of orders
    Then the response status should be 200
    And the response should contain 2 orders

  # 6. User trying to view another user's order
  Scenario: User tries to view another user's order
    Given I am authenticated as "user1" with password "pass123"
    And an order exists with id 3 for user "admin1"
    When I retrieve the order with id 3
    Then the response status should be 403
