package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

//POM
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    private int timeSeconds = 5;
    public static String durationRe = "[^\\W•]+([1-9][0-99]+|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])";
    public static String songTotalRe = "^\\d{1,}[^\\W•]";
    public static Set<String> themes = Set.of("pines","classic", "violet", "oak", "slate", "madison", "astronaut", "chocolate", "laura", "rose-petals", "purple-waves", "pop-culture", "jungle", "mountains", "nemo", "cat");
    /**
     * Navigation header "About" link
     */
    @FindBy(xpath = "//div[@class='header-right']//button[@data-testid='about-btn']")
    @CacheLookup
    private WebElement aboutBtnLocator;
    @FindBy(xpath = "//div[@class='modal-wrapper overlay']//div[@data-testid='about-modal']")
    private WebElement aboutModalLocator;
    @FindBy(xpath = "//h1[contains(text(), 'About Koel')]")
    @CacheLookup
    List<WebElement> modal;
    @FindBy(xpath = "//div[@class='modal-wrapper overlay']//footer")
    private WebElement modalCloseLocator;
    @FindBy(xpath = "//div[@class='modal-wrapper overlay']//button[@data-test='close-modal-btn']")
    @CacheLookup
    private WebElement closeModalBtn;
    /**
     * Side Menu links
     */
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='queue']")
    @CacheLookup
    private WebElement currentQueueLocator;
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='home active']")
    @CacheLookup
    private WebElement homeLocator;
    @FindBy(xpath = "//nav[@id='sidebar']//a[text()[contains(.,'All Songs')]]")
    @CacheLookup
    private WebElement allSongsLocator;
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='albums']")
    @CacheLookup
    private WebElement albumsLocator;
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='artists']")
    @CacheLookup
    private WebElement artistsLocator;
    @FindBy(xpath = "//section[@id='playlists']//li[@class='playlist favorites']/a")
    @CacheLookup
    private WebElement favoritesLocator;
    @FindBy(css = "[data-testid='sound-bar-play']")
    @CacheLookup
    private WebElement soundBarVisualizer;
    @FindBy(xpath = "//section[@id='playlists']//li[@class='playlist recently-played']/a")
    @CacheLookup
    private WebElement recentlyPlayedLocator;
    @FindBy(xpath = "//section[@id='searchExcerptsWrapper']//span[@class='details']")
    @CacheLookup
    private WebElement searchResultSongLocator;
    @CacheLookup
    @FindBy(css = ".fa-sign-out")
    private WebElement logoutButtonLocator;
    @CacheLookup
    private By closeModalButton = By.xpath("//div[@class='modal-wrapper overlay']//button[@data-test='close-modal-btn']");

    public BasePage(WebDriver givenDriver) {
        driver = givenDriver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
        PageFactory.initElements(driver, this);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }

    public boolean verifyTheme (String theme) {
        return themes.contains(theme) ? wait.until(ExpectedConditions.attributeToBe(By.xpath("//html[@data-theme]"), "data-theme", theme)) : false;
    }

    protected WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    protected WebElement findElement(WebElement webElement) {
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }
    protected void clickElement(WebElement locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
    protected List<WebElement> findElements(By locator) {
        List<WebElement> elements;
        elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        return elements;
    }
   protected boolean textIsPresent(By locator, String text) {
       return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    protected void contextClick(WebElement webElement) {
        actions.contextClick(findElement(webElement)).perform();
    }
    protected void doubleClick(WebElement webElement) {
        actions.doubleClick(findElement(webElement)).perform();
    }
    public boolean isSongPlaying() {
        findElement(soundBarVisualizer);
        return soundBarVisualizer.isDisplayed();
    }

    public void currentQueuePage () {
        actions.moveToElement(currentQueueLocator).perform();
        clickElement(currentQueueLocator);    }
    public void recentlyPlayedPage() {
        actions.moveToElement(recentlyPlayedLocator).perform();
            clickElement(recentlyPlayedLocator);
    }
    public void artistsPage() {
        actions.moveToElement(artistsLocator).perform();
        clickElement(artistsLocator);    }
    public void albumsPage() {
        actions.moveToElement(albumsLocator).perform();
        clickElement(albumsLocator);
    }
    public void allSongsPage() {  actions.moveToElement(allSongsLocator).perform();
        clickElement(allSongsLocator);
    }

    public void favorites() {actions.moveToElement(favoritesLocator).perform();
        clickElement(favoritesLocator);
    }
    public void homePage() {  actions.moveToElement(homeLocator).perform();
        clickElement(homeLocator);
    }
    public void about() { actions.moveToElement(aboutBtnLocator).perform();
            clickElement(aboutBtnLocator);
    }

    public void clickLogoutButton() {
        findElement(logoutButtonLocator).click();
    }
    public boolean checkAboutModal() {
        return findElement(aboutModalLocator).isDisplayed();
    }
    public String getSearchedSongTitle() {
            return searchResultSongLocator.getText();
        }
    public void closeModal() {
       actions.moveToElement(modalCloseLocator).perform();
       click(closeModalButton);
    }
    public void closeModalAndLogout() {
        wait.until(ExpectedConditions.visibilityOf(modalCloseLocator));
        click(closeModalButton);
        clickLogoutButton();
    }
    public boolean modalIsClosed() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        return modal.contains(closeModalBtn);
    }
    protected boolean elementDoesNotExist(By locator) {
        return findElements(locator).isEmpty();
    }
    protected void pause(int seconds) {
        actions.pause(Duration.ofSeconds(seconds)).perform();
    }


}
