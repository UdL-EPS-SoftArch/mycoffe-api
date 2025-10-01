Feature: Register Category
  In order to organize products
  As an admin
  I want to register categories and manage them

  Scenario: Register category successfully
    Given There is no registered category with name "Sweet"
    And I'm logged in as admin
    When I register a new category with name "Sweet" and description "The sweetest products"
    Then The response code is 201
    And It has been created a category with name "Sweet" and description "The sweetest products"
    And I can retrieve the category with name "Sweet"

  Scenario: Register existing category name
    Given There is a registered category with name "Sweet" and description "The sweetest products"
    And I'm logged in as admin
    When I register a new category with name "Sweet" and description "The sweet paradise for everyone"
    Then The response code is 409
    And The category description remains "The sweetest products"

  Scenario: Register category when not authenticated
    Given I'm not logged in
    When I register a new category with name "Coffee" and description "Premium coffee products"
    Then The response code is 401
    And It has not been created a category with name "Coffee"

  Scenario: Register category with empty name
    Given I'm logged in as admin
    When I register a new category with name "" and description "Premium coffee products"
    Then The response code is 400
    And The error message is "must not be blank"
    And It has not been created a category with name ""

  Scenario: Register category with empty description
    Given I'm logged in as admin
    When I register a new category with name "Pastries" and description ""
    Then The response code is 400
    And The error message is "must not be blank"
    And It has not been created a category with name "Pastries"

  Scenario: Register category with name too long
    Given I'm logged in as admin
    When I register a new category with name "ThisCategoryNameIsWayTooLongAndExceedsTheMaximumAllowedLengthForCategoryNames" and description "Delicious desserts"
    Then The response code is 400
    And The error message is "length must be between 1 and 50"
    And It has not been created a category with name "ThisCategoryNameIsWayTooLongAndExceedsTheMaximumAllowedLengthForCategoryNames"

  Scenario: Register category with description too long
    Given I'm logged in as admin
    When I register a new category with name "Beverages" and description "This description is way too long and exceeds the maximum allowed length for category descriptions in our system which should be limited to a reasonable number of characters to maintain data integrity and user experience standards"
    Then The response code is 400
    And The error message is "length must be between 1 and 255"
    And It has not been created a category with name "Beverages"

  Scenario: Register category without admin privileges
    Given I'm logged in as regular user
    When I register a new category with name "Cakes" and description "Delicious homemade cakes"
    Then The response code is 403
    And It has not been created a category with name "Cakes"
