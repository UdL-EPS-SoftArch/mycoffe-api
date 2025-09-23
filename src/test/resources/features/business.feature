@business
Feature: Business management
  As an admin
  I want to manage businesses
  So that I can keep track of their status

  Scenario: Create and retrieve a business
    Given a business with status "APPLIED"
    When I save the business
    Then I should be able to retrieve it from the repository
