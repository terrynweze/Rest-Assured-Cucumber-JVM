Feature: User Tests

@smokeTest
Scenario: Users are searchable by their username
    Given a valid user exists
    When I search for the user by their username
    Then the user is located
    
@smokeTest
Scenario: Users are able to login with valid credentials
    Given a valid user exists
    When the user submits a loggin request using 'valid' credentials
    Then the login request is successfull
    


