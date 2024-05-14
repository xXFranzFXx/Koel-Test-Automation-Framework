package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class RecentlyPlayedPage extends BasePage{
    @FindBy(linkText = "Recently Played")
    private By recentlyPlayedLocator;
    @FindBy(css = "#recentlyPlayedWrapper tr.song-item .title")
    private By recentlyPlayedTitlesLocator;
    public RecentlyPlayedPage(WebDriver givenDriver) {
         super(givenDriver);
    }

    public boolean isSearchedSongInRecentlyPlayed(String search) {
       List<WebElement> rows =  findElements(recentlyPlayedTitlesLocator);
       return rows.stream().map(WebElement::getText).anyMatch(i -> i.equals(search));
    }
}