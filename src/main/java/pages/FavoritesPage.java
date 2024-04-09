package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

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
    public int getLikedSongsCount() {
        return likedHeartIcon.size();
    }
    public FavoritesPage unlikeAllSongs()  throws StaleElementReferenceException {
        if(likedHeartIcon.isEmpty()) return this;
        int count = 0;
        int size = likedHeartIcon.size();
        try {
            for(int i = 0; i < getLikedSongsCount(); i++) {
                findElement(likedHeartIcon.get(i)).click();
                count++;
                if (getLikedSongsCount() == 0 || count == size) {
                    return this;
                }
            }
                unlikeAllSongs();
        }catch (IndexOutOfBoundsException e) {
            Reporter.log("Error removing songs from favorites playlist:  " + e, true);
        }
        return this;
    }
    public FavoritesPage contextClickFirstSong() {
        contextClick(favoritesSongList.get(0));
        return this;
    }
    public void selectDownloadFromCM() {
        WebElement selection = locateByVisibility(contextMenuDownload);
        actions.moveToElement(selection).click().perform();
    }
    public boolean checkPlaylistEmptyIcon() {
        List<WebElement> icon = findElements(By.xpath("//section[@id='favoritesWrapper']//i[@class='fa fa-frown-o']"));
        return !icon.isEmpty();
    }
    public boolean isFavoritesEmpty() {
        return favoritesSongList.isEmpty();
    }

}
