Feature: Searching the most expensive course

  Scenario: Search the most expensive course
    Given View list of courses
    When Search the most expensive course
    Then Show course
