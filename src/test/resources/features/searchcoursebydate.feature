Feature: Search course by date

  Scenario: Searching course by date
    Given We have date "18 октября"
    When Search course by date "18 октября"
    Then Logging info about searched course