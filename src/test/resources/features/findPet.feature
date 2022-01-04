@FindPet
Feature: Finding a Pet by ID

  Background: We want to search for a Pet by its ID and return it
    Given There exists a Pet named "Walter"

  Scenario: The Pet is found and returned when searched by its ID
    When Searched for the Pet by its ID
    Then The Pet named "Walter" with the same ID is found successfully
    And The Pet returned is not null

  Scenario: Pet is not found when searched by a different ID
    When Searched for the Pet by a different ID from its ID
    Then The Pet found does not have the same ID
    And The Pet found is not named "Walter"
