package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.AllSongsPage;
import pages.FavoritesPage;
import pages.LoginPage;
import util.TestUtil;
import util.listeners.TestListener;


/**
 * Story:
 * As a user I want to be able to save songs to my Favorites Playlist.
 * Acceptance Criteria:
 * Favorites playlist page should contain all songs saved as a favorite by the user
 * If no songs are marked as a favorite, the playlist page should be empty
 * User should be able to delete songs from the Favorites playlist page
 * User should be able to download songs from the Favorites playlist page
 */
public class FavoritesTests extends BaseTest {
    LoginPage loginPage;
    AllSongsPage allSongsPage;
    FavoritesPage favoritesPage;

    public void setUpFavorites() {
        loginPage = new LoginPage(getDriver());
        allSongsPage = new AllSongsPage(getDriver());
        favoritesPage = new FavoritesPage(getDriver());
        loginPage.loginValidCredentials();
    }

    @Test(description="Add songs to favorites playlist and verify they are displayed in the favorites playlist page", priority=0)
    public void likeSongs() {
        setUpFavorites();
        allSongsPage.allSongsPage();
        allSongsPage.likeOneSong();
        allSongsPage.favorites();
        TestListener.logAssertionDetails("Songs appear in favorites list after being 'liked': " + !favoritesPage.isFavoritesEmpty());
        Assert.assertFalse(favoritesPage.isFavoritesEmpty());
    }
    @Test(description = "Remove all songs from favorites playlist and verify no songs are displayed", priority=2)
    public void unlikeAllSongs(){
        setUpFavorites();
        favoritesPage.favorites();
        favoritesPage.unlikeAllSongs();
        TestListener.logAssertionDetails("User can remove songs from Favorites playlist page: " + favoritesPage.checkPlaylistEmptyIcon());
        Assert.assertTrue(favoritesPage.checkPlaylistEmptyIcon());
    }
    @Test(description = "Download a song from the favorites playlist page and verify that the song is downloading", priority=1)
    public void downloadSong() {
        setUpFavorites();
        favoritesPage.favorites();
        favoritesPage.contextClickFirstSong()
                .selectDownloadFromCM();
        TestListener.logAssertionDetails("User can download songs from Favorites playlist page: " + TestUtil.checkDownloadedFiles());
        Assert.assertTrue(TestUtil.checkDownloadedFiles());
    }
}
