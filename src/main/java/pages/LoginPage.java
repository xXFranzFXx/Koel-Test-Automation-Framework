package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage{
    @CacheLookup
    @FindBy(css = "[type='submit']")
    private WebElement submitButtonLocator;
    @CacheLookup
    @FindBy(css = "[type='email']")
    private WebElement emailField;
    @CacheLookup
    @FindBy(css = "[type='password']")
    private WebElement passwordField;
    @FindBy(xpath = "//a[@href='registration']")
    private WebElement registrationLinkLocator;
    private final By registrationLink = By.cssSelector("a[href='registration']");
    private final By emailInput = By.cssSelector("[type='email']");
    public LoginPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public void clickSubmitBtn() {
       submitButtonLocator.click();
    }
    public void enterEmail(String email) {
      driver.findElement(emailInput).sendKeys(email);
    }
    public LoginPage provideEmail(String email) {
        emailField.sendKeys(email);
        return this;
    }
    public LoginPage providePassword(String password) {
       passwordField.sendKeys(password);
       return this;
    }
    public HomePage loginValidCredentials() {
        String homeUrl = "https://qa.koel.app/#!/home";
        provideEmail(System.getProperty("koelUser"));
        providePassword(System.getProperty("koelPassword"));
        clickSubmitBtn();
        if(!driver.getCurrentUrl().equalsIgnoreCase(homeUrl)) {
            driver.get(homeUrl);
        }
        return new HomePage(driver);
    }
    public void loginAsNewUser() {
        provideEmail(System.getProperty("koelNewUser"));
        providePassword(System.getProperty("koelPassword"));
        clickSubmitBtn();
    }
    public void clickRegistrationLink() {
        registrationLinkLocator.click();
    }
    public boolean getRegistrationLink() {
     WebElement registrationLnk = find(registrationLink);
     return registrationLnk.isDisplayed();
    }
}
