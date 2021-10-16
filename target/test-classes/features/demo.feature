Feature: I open browser Chrome

  Scenario: Opening browser by choose factory
    Given We have browser "CHROME"
    When User open page url "https://otus.ru"
    Then Otus homepage is opened

