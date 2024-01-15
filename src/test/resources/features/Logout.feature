@functionalityTest @logout
Feature: Test logout button functionality
    Background: Begin tests from logged in state
      Given User is on login page
      And User is logged in

    @smoke @regression
    Scenario: User can logout after successfully logging in
      And Logout button is visible next to profile pic
      When User clicks logout button
      Then User is logged out and navigates back to login screen

    @updateProfile @e2e @smoke @ignore
    Scenario: User can logout after successfully changing profile name
      When User clicks profile pic
      Then Profile page is opened
      When User enters current password
      And  User enters updated name
      And  User clicks save button
      Then Success message is displayed
#      When Success message disappears
      And User clicks logout button
      Then User navigates back to login screen
