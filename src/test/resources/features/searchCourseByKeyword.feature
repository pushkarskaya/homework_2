Feature:Find course by keyword

  Scenario: Find course
  Given Find course "C#"
  When User click finded course "C#"
  Then Finded course is opened "C#"