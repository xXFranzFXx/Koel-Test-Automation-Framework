package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrentQueuePage extends  BasePage{
    @FindBy(xpath = "//h1[text()[normalize-space()='Current Queue']]")
    private WebElement currentQueuePageTitle;
    @FindBy(css = "table.items .song-item.playing .title")
    private WebElement currentlyPlaying;
    @FindAll({
            @FindBy(css = "#queueWrapper td.artist"),
            @FindBy(css = "#queueWrapper td.title"),
            @FindBy(css = "#queueWrapper td.album"),
            @FindBy(css = "#queueWrapper td.time.text-secondary")
    })
    private List<WebElement> columns;
    @FindBy(xpath = "//span[@class='meta text-secondary']//span[@data-test='list-meta']")
    private WebElement durationLocator;
    @FindBy(css = "#queueWrapper button.btn-shuffle-all")
    private WebElement shuffleBtn;
    @FindBy(css = "#queueWrapper button.btn-clear-queue")
    private WebElement clearBtn;
    @FindBy(css = "#queueWrapper div.text")
    private WebElement emptyQueueText;
    private final By queueTableRows = By.xpath("//section[@id='queueWrapper']//table[@class='items']/tr");
    private final By currentlyPlayingLocator = By.cssSelector("table.items .song-item.playing");


    public CurrentQueuePage(WebDriver givenDriver) {
        super(givenDriver);
    }

    public boolean isSongPlayingCQ() {
        WebElement currentSOng = wait.until(ExpectedConditions.presenceOfElementLocated(currentlyPlayingLocator));
        return findElement(currentSOng).isDisplayed();
    }
    public String duration() {
        return findElement(durationLocator).getText();
    }

    public boolean checkTotalOrDuration(String regex, String time) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);
        return matcher.find();
    }
    public String extractTotalOrDuration(String regex, String time) {
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

    public int queueListSize() {
        return findElements(queueTableRows).size();
    }

    public boolean queueListExists() {
        int li = findElements(queueTableRows).size();
        return (li > 0);
    }
    public boolean findSongInfo() {
        return columns.stream()
                .map(WebElement::getText)
                .noneMatch(String::isEmpty);
    }
    public CurrentQueuePage clickShuffleBtn() {
        clickElement(shuffleBtn);
        return this;
    }
    public String getCurrentlyPlaying() {
        return currentlyPlaying.getText();
    }
    public CurrentQueuePage clickClearBtn() {
        clickElement(clearBtn);
        return this;
    }
    public String getEmptyListText() {
        return emptyQueueText.getText();
    }

}
