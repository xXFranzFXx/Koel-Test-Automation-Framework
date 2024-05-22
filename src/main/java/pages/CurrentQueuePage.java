package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrentQueuePage extends  BasePage{
    @FindBy(xpath = "//h1[text()[normalize-space()='Current Queue']]")
    private WebElement currentQueuePageTitle;
    @FindBy(css = "table.items .song-item.playing")
    private By currentlyPlaying;
    @FindBy(xpath = "//span[@class='meta text-secondary']//span[@data-test='list-meta']")
    private WebElement durationLocator;
    private final By queueTableRows = By.xpath("//section[@id='queueWrapper']//table[@class='items']/tr");
    private final By currentlyPlayingLocator = By.cssSelector("table.items .song-item.playing");

    public CurrentQueuePage(WebDriver givenDriver) {
        super(givenDriver);
    }

    public boolean isSongPlayingCQ() {
        WebElement currentSOng = findPresentElementBy(currentlyPlayingLocator);
        return findElement(currentSOng).isDisplayed();
    }
    public String duration() {
        return getTextFromElement(durationLocator);
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
        return !findElements(queueTableRows).isEmpty();
    }
}
