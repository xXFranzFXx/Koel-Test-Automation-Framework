package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AlbumsPage extends BasePage{

    //locators
    //Albums side bar menu choice
    @FindBy(xpath = "//nav[@id='sidebar']/section[1]/ul/li[4]/a")
    private WebElement albumsLocator;

    //element to play all songs in an album
    @FindBy(xpath = "//li[text()='Play All']")
    private WebElement playAll;

    //element to shuffle all current songs playing in the album
    @FindBy(css = "li[data-test='shuffle']")
    private WebElement shuffleSongs;
    @FindBy(xpath = "//section[@id='albumsWrapper']/div/article[1]/span/span/a")
    private WebElement firstAlbumLocator;
    @FindBy(xpath = "//h1[text()[normalize-space()='Albums']]")
    private WebElement albumsPageTitleLocator;



    public AlbumsPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public AlbumsPage navigateToAlbums() {
        click((By) albumsLocator);
        return this;
    }
    public boolean checkHeaderTitle() {
        return albumsPageTitleLocator.isDisplayed();
    }

    public AlbumsPage rightClickAlbum() {
        contextClick(firstAlbumLocator);
        return this;
    }
    public void selectPlayAll() {
        findElement(playAll);
        playAll.click();
    }

    public boolean checkAlbumSongPlaying() {
        return isSongPlaying();
    }

}
