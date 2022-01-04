@SavePet
Feature: Saving Pet

  Background: We want to save a certain Pet

  Scenario: Owner found when searched by ID
    Given There exists a Pet named "Walter" and an Owner
    When Saving the Pet
    Then The Pet is added to the Owner's pets
    And The pet is stored in cache
