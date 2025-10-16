Feature: Register Basket
  In order to manage shopping carts
  As a customer
  I want to create and manage baskets

  Background:
    Given There is a registered user with username "demo" and password "password" and email "demo@email.org"
    And There is a registered customer with username "customer1" and email "customer1@email.org"

  Scenario: Register basket successfully for a customer
    Given Customer "customer1" does not have a basket
    And I login as "demo" with password "password"
    When I create a new basket for customer "customer1"
    Then The response code is 201
    And It has been created a basket for customer "customer1"

  Scenario: Cannot create duplicate basket for same customer
    Given Customer "customer1" has a basket
    And I login as "demo" with password "password"
    When I create a new basket for customer "customer1"
    Then The response code is 409

  Scenario: Retrieve basket for a customer
    Given Customer "customer1" has a basket
    And I login as "demo" with password "password"
    When I retrieve the basket for customer "customer1"
    Then The response code is 200
    And The basket belongs to customer "customer1"

  Scenario: Customer without basket
    Given Customer "customer1" does not have a basket
    And I login as "demo" with password "password"
    When I retrieve the basket for customer "customer1"
    Then The response code is 404