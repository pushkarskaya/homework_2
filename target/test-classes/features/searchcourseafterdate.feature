Feature: Searching course after date
  Scenario: Searching course after date
    Given We have afterdate "19 ноября 2021 года"
    When Search course after date "19 ноября 2021 года"
    Then Logging info about all courses after date