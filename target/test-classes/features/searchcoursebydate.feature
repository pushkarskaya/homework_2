Feature: Search course by date

  Scenario: Searching course by date
    Given We have date "30 ноября"
    When Search course by date "30 ноября"
    Then Logging info about searched course