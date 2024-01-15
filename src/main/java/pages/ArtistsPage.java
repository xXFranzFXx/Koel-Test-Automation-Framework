package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ArtistsPage extends BasePage{
    @FindBy(xpath = "//li[text()='Play All']")
    private WebElement playAllLocator;
    @FindBy(css = "li[data-test='shuffle']")
    private WebElement shuffleLocator;
    @FindBy(xpath = "//img[@alt='Sound bars']")
    private WebElement soundBarLocator;
    @FindBy(xpath = "//nav[@id='sidebar']/section[1]/ul/li[5]/a")
    private By artistsSideMenuLocator;
    @FindBy(xpath = "//h1[text()[normalize-space()='Artists']]")
    private WebElement artistsPageTitleLocator;
    //finds the link to any album on the artists page
    @FindBy(xpath = "//section[@id='artistsWrapper']/div/article[1]/span/span/a")
    private WebElement firstArtistAlbumLocator;
    public ArtistsPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public ArtistsPage navigateToArtists() {
        click(artistsSideMenuLocator);
        return this;
    }
    public boolean checkHeaderTitle() {
        return artistsPageTitleLocator.isDisplayed();
    }
    public boolean soundbarIsDisplayed() {
        return soundBarLocator.isDisplayed();
    }
    public ArtistsPage rightClickAlbum() {
        contextClick(firstArtistAlbumLocator);
        return this;
    }
    public void selectPlayAll() {
        findElement(playAllLocator);
        playAllLocator.click();
    }
}
