package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SearchPage extends BasePage{
        @FindBy(css = "#searchExcerptsWrapper  span > strong")
        private WebElement searchResultHeaderText;
        @FindBy(css ="#searchForm input[type='search']")
        private WebElement searchInputLocator;
        @FindBy(css = "section.artists > ul")
        private WebElement artistResult;
        public SearchPage(WebDriver givenDriver) {
            super(givenDriver);
        }
        public boolean isSearchResultEmpty(String section) {
            By childLocator = By.xpath("//section[@data-testid='"+section+"-excerpts']//article");
            List<WebElement> results = findElements(childLocator);
            return results.isEmpty();
        }
        public boolean noneFoundTextExists(String section) {
            List<WebElement> text = findElements(By.xpath("//section[@data-testid='"+section+"-excerpts']//p"));
            return text != null;
        }
        //Does not work because shadow content is closed
        public WebElement getShadowCancelBtn() {
            WebElement host = driver.findElement(By.cssSelector("#searchForm > input[type='search']"));
            SearchContext shadowRoot = host.getShadowRoot();
            return shadowRoot.findElement(By.cssSelector("#text-field-container #search-clear"));
        }
        public String getSearchResultHeaderText() {
            return findElement(searchResultHeaderText).getText();
        }
       //since cancel button is part of a shadow tree we have to access it by approximating its relative position with respect to the center of the search bar
        public void clickCancelBtn() {
            int width = searchInputLocator.getSize().getWidth();
            int height = searchInputLocator.getSize().getHeight();
            int center = width/2;
            int xOffset = center - 10;
            actions.moveToElement(searchInputLocator, xOffset, -1).click().perform();
        }
        public void useKeyBoardClear() {
            searchInputLocator.sendKeys(Keys.chord(Keys.CONTROL, Keys.SHIFT, "A"));
            searchInputLocator.sendKeys(Keys.BACK_SPACE);
        }
        public void invokeSearchFromKeybd() {
            pause(2);
            wait.until(ExpectedConditions.urlContains("https://qa.koel.app/#!"));
            actions.sendKeys("f").perform();
        }
    public boolean artistExists() {
        return artistResult.isDisplayed();
    }
}
