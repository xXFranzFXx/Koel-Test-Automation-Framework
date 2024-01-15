@functionalityTest @login
Feature: Login feature

  Background: Test multiple scenarios that may be encountered when logging in
    Given User is on login page

 @smoke @regression
  Scenario Outline: Log in Success
    When User enters email "<email>"
    And  User enters password "<password>"
    And  User clicks submit
    Then User should navigate to home page
  Examples:
    | email | password |
    | franz.fernando+1@testpro.io | te$t$tudent1 |

  @regression
  Scenario Outline: Log in with invalid email input and/or invalid password input
    When User enters email "<email>"
    And User enters password "<password>"
    And User clicks submit
    Then User should still be on Login page
    Examples:
      | email                | password           |
      | franz.fernando+1@testprio  | te$t$tudent1       |
      | franz.fernando+1@testpro.io| te$t$tudent        |
      |                      | te$t$tudent1       |
      | franz.fernando+1@testpro.io |                    |
      |                      |                    |

  @updateProfileAll @smoke @e2e @ignore
  Scenario Outline: Log in and update email address and password, log out and attempt to log in with old email address and old password
    Given User logs in
    When User clicks profile pic
    Then Profile page is opened
    When User provides current password "<password>"
    And User provides new email address "<newEmail>"
    And User provides new password "<newPasswd>"
    And User clicks save button
    Then Success message is displayed
    When Success message disappears
    And User clicks logout button
    Then User is logged out and navigates back to login screen
    When User enters email "<email>"
    And  User enters password "<password>"
    And User clicks submit
    Then User should still be on Login page
    Examples:
    | email | newEmail | password| newPasswd |
    | franz.fernando+1@testpro.io | updated.email@testpro.io | te$t$tudent1 | te$t$tudent2 |

  @resetProfile @ignore
  Scenario Outline: Reset profile back to original email
    When User enters email "<newEmail>"
    And  User enters password "<password>"
    And  User clicks submit
    Then User should navigate to home page
    When User clicks profile pic
    Then Profile page is opened
    When User provides current password "<password>"
    And User provides new email address "<oldEmail>"
    And User provides new password "<oldPasswd>"
    And User clicks save button
    Then Success message is displayed
    Examples:
      | oldEmail | newEmail | password| oldPasswd |
      | franz.fernando+1@testpro.io | updated.email@testpro.io | te$t$tudent2 | te$t$tudent1 |


