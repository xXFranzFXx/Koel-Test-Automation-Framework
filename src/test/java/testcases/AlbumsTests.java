package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.AlbumsPage;
import pages.HomePage;
import pages.LoginPage;
import util.listeners.TestListener;

public class AlbumsTests extends BaseTest {
    LoginPage loginPage;
    AlbumsPage albumsPage;
    HomePage homePage;
    public AlbumsTests() {
        super();
    }
    public void setupAlbums(){
        loginPage = new LoginPage(getDriver());
        loginPage.loginValidCredentials();
        homePage = new HomePage(getDriver());
        albumsPage = new AlbumsPage(getDriver());
        homePage.albumsPage();
    }
    @Test(description = "Right click on an album and play all songs")
    public void playFirstAlbumSongs() {
        setupAlbums();
        albumsPage.rightClickAlbum()
                .selectPlayAll();
        TestListener.logAssertionDetails("Added all songs from selected album to current queue: " + albumsPage.checkAlbumSongPlaying());
        Assert.assertTrue(albumsPage.checkAlbumSongPlaying());
    }
    @Test(description = "Verify albums are displayed")
    public void albumsAreDisplayed() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums are present: " + albumsPage.albumsArePresent());
        Assert.assertTrue(albumsPage.albumsArePresent(), "Albums are not displayed");
    }
    @Test(description = "Verify all albums display album titles")
    public void checkAlbumTitles() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums display album titles: " + albumsPage.checkAlbumTitles());
        Assert.assertTrue(albumsPage.checkAlbumTitles(), "One or more albums is not displaying a title");
    }
    @Test(description = "Verify albums have a cover image")
    public void checkAlbumCoverImages() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums have a cover image: " + albumsPage.checkAllAlbumCoverImage());
        Assert.assertTrue(albumsPage.checkAllAlbumCoverImage(), "One or more albums does not have a cover image");
    }
    @Test(description = "Verify albums have album artist")
    public void checkAlbumArtist() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums have an artist: " + albumsPage.checkAlbumArtists());
        Assert.assertTrue(albumsPage.checkAlbumArtists(), "One or more albums does not have an artist");
    }
    @Test(description = "Verify albums have song track total")
    public void checkAlbumSongTrackTotal() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums have total song count: " + albumsPage.albumSongTrackTotal());
        Assert.assertTrue(albumsPage.albumSongTrackTotal(), "One or more albums does not list song track total");
    }
    @Test(description="Verify albums have song duration total")
    public void checkAlbumSongDurtation() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums have song duration total: " + albumsPage.albumDuration());
        Assert.assertTrue(albumsPage.albumDuration(), "One or more albums does not list total song duration");
    }
    @Test(description = "Verify each album has a download button")
    public void checkDownloadButton() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums have a download button: " + albumsPage.checkDownloadButtons());
        Assert.assertTrue(albumsPage.checkDownloadButtons(), "One or more albums is missing a download button");
    }
    @Test(description = "Verify each album has a shuffle button")
    public void checkShuffleButton() {
        setupAlbums();
        TestListener.logAssertionDetails("All albums have a shuffle button: " + albumsPage.checkShuffleButtons());
        Assert.assertTrue(albumsPage.checkShuffleButtons(), "One or more albums is missing a shuffle button");
    }
    @Test(description = "Verify 'go to album' context menu option")
    public void goToAlbumCM() {
        setupAlbums();
        String albumTitle = albumsPage.getAlbumTitle(0);
        String albumViewTitle = albumsPage.getAlbumViewTitle();
        albumsPage.rightClickAlbum()
                .goToAlbumFromCM();
        TestListener.logAssertionDetails("Go to Album context menu button displays correct album: " + albumTitle.equalsIgnoreCase(albumViewTitle));
        Assert.assertEquals(albumTitle, albumViewTitle);
    }
}
