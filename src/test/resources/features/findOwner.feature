Feature: Finding Owner

  Background: We want to find existing Owner by his or her ID
    Given There is one Owner with a certain ID

  Scenario: Owner found when searched by ID
    When Searched for this Owner by his or her ID
    Then The Owner with the same ID is found successfully

  Scenario: Owner is not found when searched by different ID
    When Searched for this Owner by a different ID from his or her ID
    Then The Owner found is not the one we searched for
