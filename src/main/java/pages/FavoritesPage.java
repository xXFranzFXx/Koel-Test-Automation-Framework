package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FavoritesPage extends BasePage{
    @FindBy(xpath = "//section[@id='favoritesWrapper']//td[@class='favorite']//button[@class='text-secondary' and contains(@title, 'Unlike')]")
    private List<WebElement> likedHeartIcon;
    @FindBy(xpath = "//nav[@data-testid='song-context-menu']//li[@class='download']")
    private WebElement contextMenuDownload;
    @FindBy(xpath = "//section[@id='favoritesWrapper']//table[@class='items']/tr")
    private List<WebElement> favoritesSongList;
    @FindBy(xpath = "//section[@id='favoritesWrapper']//i[@class='fa fa-frown-o']")
    private By frownIcon;
    public FavoritesPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public FavoritesPage unlikeAllSongs()  throws StaleElementReferenceException {
        if(likedHeartIcon.isEmpty()) return this;
        try {
            for (WebElement l : likedHeartIcon) {
                findElement(l).click();
            }
        }catch (StaleElementReferenceException e) {
            Reporter.log("Error removing songs from favorites playlist:  " + e, true);
        }
        return this;
    }
    public FavoritesPage contextClickFirstSong() {
        contextClick(favoritesSongList.get(0));
        return this;
    }
    public FavoritesPage selectDownloadFromCM() {
        WebElement selection = wait.until(ExpectedConditions.visibilityOf(contextMenuDownload));
        actions.moveToElement(selection).click().perform();
        return this;
    }
    public boolean checkPlaylistEmptyIcon() {
        List<WebElement> icon = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//section[@id='favoritesWrapper']//i[@class='fa fa-frown-o']")));
        return !icon.isEmpty();
    }
    public boolean isFavoritesEmpty() {
        return favoritesSongList.isEmpty();
    }

}
