Feature: Register Order
  In order to manage my orders
  As a user
  I want to register new orders


  Scenario: Register a new order successfully
    Given I login as "demo" with password "password"
    When I register a new order with id "1"
    Then The response code is 201


  Scenario: Register a new order that already exists
    Given I login as "demo" with password "password"
    When I register a new order with id "1"
    Then The response code is 409
    And The order with id "2" is not registered


  Scenario: Register a order when not authenticated
    Given I'm not logged in
    When I register a new order with id "3"
    Then The response code is 403
    And The order with name "3" is not registered


  Scenario: Register a order with empty name
    Given I login as "demo" with password "password"
    When I register a new order with id ""
    Then The response code is 400
    And The order with id "" is not registered


  ## TODO check user roles
  ## check db initialization records
  ## check if ...