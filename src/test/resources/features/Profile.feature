@functionalityTest @profile
Feature: Testing functionality of Profile page
  Background:
    Given User logs in
    And User navigates to profile page

    @regression
    Scenario Outline: User can update the theme
      When User clicks on a theme "<theme>"
      Then The profile theme will be updated to "<theme>"

      Examples:
      |theme|
      |pines|
      |classic|
      |violet |
      |oak    |
      |slate  |
      |madison|
      |astronaut|
      |chocolate|
      |laura    |
      |rose-petals|
      |purple-waves|
      |pop-culture |
      |jungle      |
      |mountains   |
      |nemo        |
      |cat         |

    @ignore #bug, koel allows user to update email with invalid format i.e. testpro@testpro
    Scenario Outline: User tries to update email with invalid formatted email
      When User provides current password "<password>"
      And User provides new email address "<newEmail>"
      And User clicks save
      Then User receives error message

      Examples:
      |newEmail|password|
      | testpro.com |te$t$tudent1 |
      | email@      |te$t$tudent1 |
      |         |te$t$tudent1 |
      | testpro@testpro |te$t$tudent1 |

