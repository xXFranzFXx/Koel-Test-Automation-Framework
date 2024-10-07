package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProfilePage extends BasePage {
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
    @FindBy(css = "#profileWrapper [name='notify']")
    private WebElement notificationCheckBox;
    @FindBy(css = "#profileWrapper [name='rm_closing']")
    private WebElement confirmCloseCheckBox;
    @FindBy(css = "#profileWrapper [name='show_album_art_overlay']")
    private WebElement showAlbumArtCheckBox;
    @FindBy(xpath = "//section[@id='profileWrapper']//input[@type='checkbox']")
    private List<WebElement> checkboxes;


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
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("i.fa.fa-sign-out"))).click();
    }

    public ProfilePage provideNewPassword(String newPassword) {
        WebElement currentPassword = find(By.cssSelector("#inputProfileNewPassword"));
        currentPassword.clear();
        currentPassword.sendKeys(newPassword);
        return this;
    }

    public void clickTheme(String theme) {
        By newTheme = By.xpath("//div[@data-testid='theme-card-" + theme + "']");
        WebElement themeLocator = find(newTheme);
        findElement(themeLocator).click();
    }

    public boolean checkTheme(String theme) {
        return waitForAttribute(By.xpath("//html[@data-theme]"), "data-theme", theme);
    }

    public String invalidEmailMsg() {
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

    public Boolean checkBoxState(WebElement element) {
        return element.isSelected();
    }

    public void clickCheckBox(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        actions.moveToElement(element).click().perform();
    }

    public Boolean notificationState() {
        return checkBoxState(notificationCheckBox);
    }

    public ProfilePage clickNotificationCheckBox() {
        clickCheckBox(notificationCheckBox);
        return this;
    }

    public Boolean confirmCloseState() {
        return checkBoxState(confirmCloseCheckBox);
    }

    public ProfilePage clickConfirmBox() {
        clickCheckBox(confirmCloseCheckBox);
        return this;
    }
    public Boolean showArtState() {
        return checkBoxState(showAlbumArtCheckBox);
    }

    public ProfilePage clickShowArtCheckBox() {
        clickCheckBox(showAlbumArtCheckBox);
        return this;
    }
    public List<Boolean> getCheckboxStatus() {
        List<Boolean> checkBoxStatus = new ArrayList<>();
        checkBoxStatus = checkboxes.stream().map(WebElement::isSelected).toList();
        return checkBoxStatus;
    }
    public ProfilePage clickAllCheckBoxes() {
        checkboxes.forEach(WebElement::click);
        return this;
    }
    public List<String> getCheckBoxNames() {
        return checkboxes.stream().map(el -> el.getAttribute("name")).toList();
    }
    public Map<String, Boolean> checkBoxMap() {
        return IntStream.range(0, getCheckBoxNames().size())
                .boxed()
                .collect(Collectors.toMap(
                        getCheckBoxNames()::get,
                        getCheckboxStatus()::get));
    }
}
