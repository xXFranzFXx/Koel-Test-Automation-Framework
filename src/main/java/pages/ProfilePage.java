package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProfilePage extends BasePage{
    @FindBy(xpath = "//div[@data-testid='theme-card-pines']/div")
    private WebElement inThePinesThemeLocator;
    @FindBy(css = ".success.show")
    private WebElement updateNotification;
    @FindBy(xpath = "//span[@id='userBadge']//a[@data-testid='view-profile-link']/span")
    private WebElement actualProfileName;
    @FindBy(css = "button.btn-submit")
    private WebElement saveButton;
    @FindBy(id = "inputProfileEmail")
    private WebElement emailId;
    @FindBy(css = ".error.show")
    private WebElement errorNotification;
    @FindBy(css = "input[name='notify']")
    private WebElement notificationCheckBox;
    @FindBy(css = "input[name='rm_closing']")
    private WebElement confirmCloseCheckBox;
    @FindBy(css = "input[name='show_album_art_overlay']")
    private WebElement showAlbumArtCheckBox;



    public ProfilePage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public void pinesTheme() {
        inThePinesThemeLocator.click();
    }
    public ProfilePage provideRandomProfileName(String randomName) {
        WebElement profileName = find(By.cssSelector("[name='name']"));
        profileName.clear();
        profileName.sendKeys(randomName);
        return this;
    }
    public boolean clickSave() {
      findElement(saveButton).click();
      return updateNotification.isDisplayed();
    }
    public String getProfileName() {
       return findElement(actualProfileName).getText();
    }
    public ProfilePage provideNewEmail(String updatedEmail) {
        WebElement emailInput = findPresentElementBy(By.cssSelector("#inputProfileEmail"));
        emailInput.clear();
        emailInput.sendKeys(updatedEmail);
        return this;
    }
    public ProfilePage clickSaveButton() {
       findElement(saveButton).click();
       return this;
    }
    public ProfilePage provideCurrentPassword(String password) {
        WebElement currentPassword = find(By.cssSelector("#inputProfileCurrentPassword"));
        currentPassword.clear();
        currentPassword.sendKeys(password);
        return this;
    }
    public boolean notificationPopup() {
       WebElement notification = findElement(updateNotification);
        return notification.isDisplayed();
    }
    public boolean notificationHasDisappeared() {
        return wait.until(ExpectedConditions.invisibilityOf(updateNotification));
    }
    public void clickLogout() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".success.show")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector( "i.fa.fa-sign-out"))).click();
    }
    public ProfilePage provideNewPassword(String newPassword) {
        WebElement currentPassword = find(By.cssSelector("#inputProfileNewPassword"));
        currentPassword.clear();
        currentPassword.sendKeys(newPassword);
        return this;
    }
    public void clickTheme(String theme) {
        By newTheme = By.xpath( "//div[@data-testid='theme-card-"+theme+"']");
        WebElement themeLocator = find(newTheme);
        findElement(themeLocator).click();
    }
    public boolean checkTheme (String theme) {
        return waitForAttribute(By.xpath("//html[@data-theme]"), "data-theme", theme);
    }
    public String invalidEmailMsg () {
       return getAttribute(emailId, "required");
    }
    public void moveToSaveAndClick() {
       actions.moveToElement(saveButton).click().build().perform();
    }
    public ProfilePage provideEmail(String email) {
        WebElement currentEmail = find(By.cssSelector("#inputProfileEmail"));
        currentEmail.clear();
        currentEmail.sendKeys(email);
        return this;
    }
    public String getValidationMsg() {
        return getAttribute(emailId, "validationMessage");
    }
    public String getErrorNotificationText() {
        WebElement errorMsg = findElement(errorNotification);
        return errorMsg.getText();
    }
    public Boolean checkBoxInitialState(WebElement element) {
        return element.isSelected();
    }
    public Boolean checkBoxStateAfter(WebElement element) {
        findElement(element).click();
        return element.isSelected();
    }
}
