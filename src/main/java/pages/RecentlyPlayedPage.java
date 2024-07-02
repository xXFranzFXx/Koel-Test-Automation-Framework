package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class RecentlyPlayedPage extends BasePage{
    @FindBy(linkText = "Recently Played")
    private By recentlyPlayedLocator;
    private By recentlyPlayedTitlesLocator = By.cssSelector("#recentlyPlayedWrapper tr.song-item .title");
    public RecentlyPlayedPage(WebDriver givenDriver) {
         super(givenDriver);
    }
    public boolean isSearchedSongInRecentlyPlayed(String search) {
       List<WebElement> rows =  findElements(recentlyPlayedTitlesLocator);
       int fails = rows.stream()
               .filter(row -> !row.getText().equals(search))
                        .toList()
                        .size();
                return fails == 0;
    }
    public List<String> recentlyPlayedSongs() {
        List<WebElement> songTitles = findElements(recentlyPlayedTitlesLocator);
        return songTitles.stream().map(WebElement::getText).toList();
    }
}