package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import pages.AlbumsPage;
import pages.HomePage;
import pages.LoginPage;
import util.listeners.TestListener;

import java.net.MalformedURLException;

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
        albumsPage = new AlbumsPage(getDriver());
    }

    @Test(description = "Right click on an album and play all songs")
    public void playFirstAlbumSongs() {
        setupAlbums();
        loginPage.albumsPage();
                albumsPage.rightClickAlbum()
                .selectPlayAll();
        TestListener.logAssertionDetails("Added all songs from selected album to current queue: " + albumsPage.checkAlbumSongPlaying());
        Assert.assertTrue(albumsPage.checkAlbumSongPlaying());
    }
    @Test(description = "Verify albums are displayed")
    public void albumsAreDisplayed() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums are present: " + albumsPage.albumsArePresent());
        Assert.assertTrue(albumsPage.albumsArePresent(), "Albums are not displayed");
    }
    @Test(description = "Verify all albums display album titles")
    public void checkAlbumTitles() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums display album titles: " + albumsPage.checkAlbumTitles());
        Assert.assertTrue(albumsPage.checkAlbumTitles(), "One or more albums is not displaying a title");
    }
    @Test(description = "Verify albums have a cover image")
    public void checkAlbumCoverImages() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums have a cover image: " + albumsPage.checkAllAlbumCoverImage());
        Assert.assertTrue(albumsPage.checkAllAlbumCoverImage(), "One or more albums does not have a cover image");
    }
    @Test(description = "Verify albums have album artist")
    public void checkAlbumArtist() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums have an artist: " + albumsPage.checkAlbumArtists());
        Assert.assertTrue(albumsPage.checkAlbumArtists(), "One or more albums does not have an artist");
    }
    @Test(description = "Verify albums have song track total")
    public void checkAlbumSongTrackTotal() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums have total song count: " + albumsPage.albumSongTrackTotal());
        Assert.assertTrue(albumsPage.albumSongTrackTotal(), "One or more albums does not list song track total");
    }
    @Test(description="Verify albums have song duration total")
    public void checkAlbumSongDurtation() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums have song duration total: " + albumsPage.albumDuration());
        Assert.assertTrue(albumsPage.albumDuration(), "One or more albums does not list total song duration");
    }
    @Test(description = "Verify each album has a download button")
    public void checkDownloadButton() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums have a download button: " + albumsPage.checkDownloadButtons());
        Assert.assertTrue(albumsPage.checkDownloadButtons(), "One or more albums is missing a download button");
    }
    @Test(description = "Verify each album has a shuffle button")
    public void checkShuffleButton() {
        setupAlbums();
        loginPage.albumsPage();
        TestListener.logAssertionDetails("All albums have a shuffle button: " + albumsPage.checkShuffleButtons());
        Assert.assertTrue(albumsPage.checkShuffleButtons(), "One or more albums is missing a shuffle button");
    }

}
