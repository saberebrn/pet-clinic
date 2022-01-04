Feature: Adding new Pet to an Owner

  Background: We want to create a new Pet and add it to a certain Owner
    Given There exists one Owner with a certain ID

  Scenario: new Pet is created and added to the Owner
    When new Pet is added to the Owner
    Then The Owner will have a new Pet
    And Returned Pet will not be null

  Scenario: new Pet is created and added to the very first Owner not a different Owner with a certain name
    Given an Owner exists with first name "Patrick" last name "Steward"
    When new Pet is added to the very first Owner
    Then The different Owner will not have the new Pet
