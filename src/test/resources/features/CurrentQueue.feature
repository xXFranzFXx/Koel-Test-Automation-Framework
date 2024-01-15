Feature: Current Queue
  Background:
  As a user, I want to be able to open Current Queue page to be able to see all currently played songs
  Acceptance Criteria:
  Current Queue page should display currently played songs by a user in the app
  The total count of songs should be displayed
  The total duration count of all songs should be displayed
  ID, Title, Artist, Album, and Time should be correctly displayed
  User should be navigated to the Current Queue page after double clicking on the song on the following pages: Home, All songs, Albums, Artists, Favorites, Recently played, and user's created playlist
  'Shuffle All' button should shuffle songs
  The page should be empty after clicking on 'Clear' button. 'No songs queued. How about shuffling all songs?' message should appear.
  Songs should appear on the Current Queue page  when clicking on the 'shuffling all songs'

  Given User is logged in

    Scenario: Queue page should display info about currently playing songs
      Given User has songs playing
      When User navigates to Current Queue page
      Then Currently playing songs should be listed
      And Total count of songs should be displayed
      And Duration of count of all songs should be displayed
      And ID, Title, Artist, ALbum and Time should be displayed

    Scenario Outline: Double clicking a song from any of the locations in the side menu will navigate user to Current Queue page
      Given User clicks on "<location>"
      Then User will navigate to "<location>" page
      When User double clicks on a song
      Then User will navigate to Current Queue page
      Examples:
      |location|
      |Home    |
      |All Songs|
      |ALbums   |
      |Artists  |
      |Recently Played|
      |playlist       |

    Scenario: Clicking the 'Shuffle All' button should shuffle songs
      Given User has songs playing
      Then User will navigate to Current Queue page
      When User clicks 'Shuffle All' button
      Then Currently playing songs will be shuffled

    Scenario: Clicking the 'Clear' button will clear the page and a placeholder message will be displayed
      Given User has songs playing
      Then User will navigate to Current Queue page
      When User clicks 'Clear' button
      Then Songs will disappear
      And the message "<No songs queued. How about shuffling all songs?>" will appear



