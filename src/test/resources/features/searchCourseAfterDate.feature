Feature: Searching course after date
  Scenario: Searching course after date
    Given We have afterdate "1 декабря 2021 года"
    When Search course after date "1 декабря 2021 года"
    Then Logging info about all courses after date