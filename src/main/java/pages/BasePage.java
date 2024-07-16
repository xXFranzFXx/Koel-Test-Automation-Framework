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


//POM
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    private int timeSeconds = 5;
    public static String durationRe = "[^\\W•]+([1-9][0-99]+|[01]?[0-9]):([0-5]?[0-9]):([0-5]?[0-9])";
    public static String songTotalRe = "^\\d{1,}[^\\W•]";
    public static List<String> themes = List.of("pines","classic", "violet", "oak", "slate", "madison", "astronaut", "chocolate", "laura", "rose-petals", "purple-waves", "pop-culture", "jungle", "mountains", "nemo", "cat");
    /**
     * Navigation header "About" link
     */
    @CacheLookup
    @FindBy(xpath = "//div[@class='header-right']//button[@data-testid='about-btn']")
    private WebElement aboutBtnLocator;
    @FindBy(xpath = "//div[@class='modal-wrapper overlay']//div[@data-testid='about-modal']")
    private WebElement aboutModalLocator;
    @CacheLookup
    @FindBy(xpath = "//h1[contains(text(), 'About Koel')]")
    private List<WebElement> modal;
    @FindBy(xpath = "//div[@class='modal-wrapper overlay']//footer")
    private WebElement modalCloseLocator;
    @CacheLookup
    @FindBy(xpath = "//div[@class='modal-wrapper overlay']//button[@data-test='close-modal-btn']")
    private WebElement closeModalBtn;
    /**
     * Side Menu links
     */
    @CacheLookup
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='queue']")
    private WebElement currentQueueLocator;
    @CacheLookup
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='home active']")
    private WebElement homeLocator;
    @CacheLookup
    @FindBy(xpath = "//nav[@id='sidebar']//a[text()[contains(.,'All Songs')]]")
    private WebElement allSongsLocator;
    @CacheLookup
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='albums']")
    private WebElement albumsLocator;
    @CacheLookup
    @FindBy(xpath = "//nav[@id='sidebar']//a[@class='artists']")
    private WebElement artistsLocator;
    @CacheLookup
    @FindBy(xpath = "//section[@id='playlists']//li[@class='playlist favorites']/a")
    private WebElement favoritesLocator;
    @CacheLookup
    @FindBy(css = "[data-testid='sound-bar-play']")
    private WebElement soundBarVisualizer;
    @CacheLookup
    @FindBy(xpath = "//section[@id='playlists']//li[@class='playlist recently-played']/a")
    private WebElement recentlyPlayedLocator;
    @CacheLookup
    @FindBy(xpath = "//section[@id='searchExcerptsWrapper']//span[@class='details']")
    private WebElement searchResultSongLocator;
    @CacheLookup
    @FindBy(css = ".fa-sign-out")
    private WebElement logoutButtonLocator;
    @CacheLookup
    private By pageTheme = By.xpath("//html[@data-theme]");
    @CacheLookup
    private By closeModalButton = By.xpath("//div[@class='modal-wrapper overlay']//button[@data-test='close-modal-btn']");

    public BasePage(WebDriver givenDriver) {
        driver = givenDriver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
//        PageFactory.initElements(driver, this);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 10), this);
    }
    public WebElement locateByVisibility(WebElement webElement){
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }
    public boolean verifyTheme (String theme) {
         return themes.stream().anyMatch(e -> waitForAttribute(pageTheme, "data-theme", theme));
    }
    protected void moveToElement(WebElement webElement) {
       actions.moveToElement(webElement).perform();
    }
    protected boolean waitForAttribute(By locator, String attribute, String value) {
       return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }
    protected WebElement findPresentElementBy(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    protected WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    protected WebElement findElement(WebElement webElement) {
        return wait.until(ExpectedConditions.visibilityOf(webElement));
    }
    protected String getAttribute(WebElement webElement, String attribute) {
       return findElement(webElement).getAttribute(attribute);
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
    protected void waitForText(WebElement webElement, String text){
        wait.until(ExpectedConditions.textToBePresentInElement(webElement, text));
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
        pause(2);
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
    protected String getTextFromElement(WebElement webElement) {
        return webElement.getText();
    }
    protected boolean elementDoesNotExist(By locator) {
        return findElements(locator).isEmpty();
    }
    public void pause(int seconds) {
        actions.pause(Duration.ofSeconds(seconds)).perform();
    }
}
