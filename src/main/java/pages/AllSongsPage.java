package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.Reporter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllSongsPage extends BasePage{

    @FindBy(xpath = "//h1[text()[normalize-space()='All Songs']]")
    private WebElement allSongsPageTitleLocator;
    @FindBy(css = ".all-songs tr.song-item:nth-child(1)")
    private WebElement firstSongElementLocator;
    @FindBy(css = "li.playback")
    private WebElement choosePlayAllLocator;
    @FindBy(xpath = "//nav[@id='sidebar']/section[@class='music']/ul/li[3]/a")
    private WebElement allSongsLocator;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary' and contains(@title, 'Like')]")
    private WebElement singleLikeButton;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary']")
    private List<WebElement> likeButtons;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary' and contains(@title, 'Like')]")
    private List<WebElement> unLikedButton;
    @FindBy(xpath = "//section[@id='songsWrapper']//tr[@class='song-item']//button[@class='text-secondary' and contains(@title, 'Unlike')]")
    private List<WebElement> likedSongsButton;
    @FindAll({
            @FindBy(css = "td.artist"),
            @FindBy(css = "td.title"),
            @FindBy(css = "td.album"),
            @FindBy(css = "td.time.text-secondary")
    })
    private List<WebElement> columns;
    @FindBy(css = "#songsWrapper table.items tr.song-item")
    private List<WebElement> tableRows;
    @FindBy(css = "#songsWrapper span.meta.text-secondary span")
    private WebElement headerTotalDurationText;
    private final String durationRe = "[^\\W•]+([1-9][0-99]+|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])";
    private final String songTotalRe = "^\\d{1,}[^\\W•]";

    public AllSongsPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public boolean checkSongPlaying() {
        return isSongPlaying();
    }
    public AllSongsPage navigateToAllSongs() {
        allSongsLocator.click();
        return this;
    }
    public AllSongsPage checkHeaderTitle() {
        findElement(allSongsPageTitleLocator);
        Assert.assertTrue(allSongsPageTitleLocator.isDisplayed());
        return this;
    }
    public AllSongsPage contextClickFirstSong() {
        contextClick(firstSongElementLocator);
        return this;
    }
    //unlikes every liked song
    public AllSongsPage unlikeSongs() {
        if(likedSongsButton.isEmpty()) return this;
        likedSongsButton.forEach(WebElement::click);
        return this;
    }
    public AllSongsPage likeOneSong() {
        if(unLikedButton.isEmpty()) {
            return this;
        } else {
            findElement(singleLikeButton).click();
        }
            return this;
    }
    //likes every song
    public AllSongsPage likeSongs() {
        if(unLikedButton.isEmpty()) return this;
        unLikedButton.forEach(WebElement::click);
        return this;
    }
    //checks if there are any songs that are marked as "liked"
    public boolean checkUnliked() {
        return likedSongsButton.isEmpty();
    }

    public void choosePlayOption() {
       choosePlayAllLocator.click();
    }
    public boolean findSongInfo() {
        return columns.stream()
                .map(WebElement::getText)
                .noneMatch(String::isEmpty);
    }
    public int getTotalSongsCount() {
        System.out.println(tableRows.size());
        return tableRows.size();
    }
    public String duration() {
        return findElement(headerTotalDurationText).getText();
    }
    private boolean checkTotalOrDuration(String regex, String time) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);
        return matcher.find();
    }
    private String extractTotalOrDuration(String regex, String time) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);
        if (matcher.find()) {
            Reporter.log("extracted" + matcher.group(), true);
            return matcher.group();
        } else {
            Reporter.log("Did not find correctly formatted  duration", true);
            return null;
        }
    }
    public boolean songTotalIsDisplayed() {
        return checkTotalOrDuration(durationRe, duration());
    }
    public boolean totalDurationIsDisplayed() {
        return checkTotalOrDuration(songTotalRe, duration());
    }
    public String getSongTotalFromHeader() {
        return extractTotalOrDuration(songTotalRe, duration());
    }
    public String getDurationFromHeader() {
        return extractTotalOrDuration(durationRe, duration());
    }
}
