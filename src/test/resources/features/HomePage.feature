@homePage
Feature: Homepage
  Background:
  As a user, I want to be able to open Homepage
  Acceptance Criteria:
  Welcome message for a new user should be the following: 'Hello, Student!'
  Recently played songs should be present if user played at least one song
  'View All' button should be displayed next to Recently played songs
  Recently added songs should be present
  Album name should be displayed for the Recently added songs
  Download and Shuffle icons should be present for Recently added songs
  Search field should be present and user should be able to access Search from the Homepage
  Your music panel should include the following pages: Home, Current Queue, All Songs, Albums, Artists
  PLAYLISTS panel should include:  button for new playlist creation and new smart playlist
  Playlists panel should include, Favorites playlist, Recently played playlist, smart playlists and user's created playlists
  Profile link, Logout and About Koel icons should be displayed and linked to correct pages.

    Rule: User logs in as a new user
      - User should see a welcome message
      - User should have no recently played songs
      Background: New user logs in
        Given User logs in as new user

      @ignore @newUser
      Scenario Outline: User will see a welcome message for new users
        Given User is on homepage
        Then User profile name should be "<profileName>"

  Examples:
        |profileName|
        |student|

      @ignore @newUser
      Scenario: Recently played songs should be present if user plays at least one song
        Given User is on homepage
        Then User should see not see recently played songs

    Rule: User logs in as regular user
      - User has already played a song
      - User has updated profile name
      - User has already "liked" a song
      Background: Regular user logs in as regular user
        Given User logs in
        And User is on homepage

      Scenario: User profile name no longer is "student"
        Then User will not see same welcome message as new user

    Scenario: Recently played songs should be present if user played at least one song
       Then User should see recently played songs

    Scenario: User should see "View All" button next to "Recently Played" list
       When User clicks View All button
       Then User will navigate to Recently Played page

    Scenario: User should see "Recently Added" list with basic functionality on homepage
      Given Recently Added songlist exists
      Then Album name should be displayed for each song
      And Download and Shuffle icons will be visible on hovered song

     @navigationHeader
     Scenario: Test profile link functionality
       When User clicks profile link
       Then User will be on profile page

      @navigationHeader
     Scenario:  Test "About Koel" link
       When User clicks About link
       When User clicks close
       Then The Modal will disappear

      @navigationHeader
     Scenario: User can successfully log out from home page
       When User clicks on logout link
       Then User will be logged out and navigate to login screen

      @createPlaylist
      Scenario: User can create a playlist
        When User clicks the Create a new playlist button next to the PLAYLISTS menu header in the side menu
        And User selects New Playlist in the context menu
        And User enters playlist name in the input field followed by pushing ENTER key
        Then A new playlist will be listed in the side menu

     @navigation
     Scenario: User can navigate to "Home" from side menu
       When User clicks Home in the left side navigation panel
       Then User navigates to home page

      @navigation
      Scenario: User can navigate to "All Songs" from side menu
       When User clicks All Songs in the left side navigation panel
       Then User navigates to All Songs page

      @navigation
      Scenario: User can navigate to "Albums" from side menu
       When User clicks Albums in the left side navigation panel
       Then User navigates to Albums page

      @navigation
      Scenario: User can navigate to "Artists" from side menu
       When User clicks Artists in the left side navigation panel
       Then User navigates to Artists page

      @navigation
      Scenario: User can navigate to "Recently Played" from side menu
        When User clicks Recently Played in the left side navigation panel
        Then User will navigate to Recently Played page

      @navigation
      Scenario: User can navigate to "Favorites" from side menu
        When User clicks Favorites in the left side navigation panel
        Then User will navigate to Favorites page

     @search
     Scenario: Test the functionality of search bar
       When User executes a search
       Then Search results will be displayed




      @createSmartPlaylist
      Scenario: User can create a smart playlist
        When User clicks the Create a new playlist button next to the PLAYLISTS menu header in the side menu
        And User selects New Smart Playlist in the context menu
        Then A New Smart Playlist modal will appear
        When User enters smart playlist name in the Name input field
        And User enters song criteria in the criteria input field
        And User clicks Save button
        Then A new smart list will be in the side menu






