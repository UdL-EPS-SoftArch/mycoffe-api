
@business
Feature: Business creation
  As a developer
  I want to create a Business object
  So that it is created correctly

  Scenario: Create a Business object
    Given a Business object with status "APPLIED"
    When I save the business
    Then the Business object should have status "APPLIED"