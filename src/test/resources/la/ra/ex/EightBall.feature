Feature: Magic Eight Ball

  Scenario: No Query
    Given no query
    When a request is made
    Then a response is received

  Scenario: With Query
    Given the query "Will I win the lottery?"
    When a request is made
    Then a response is received
    And the query is in the response