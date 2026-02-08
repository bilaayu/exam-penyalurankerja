package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class GistListPage {
    WebDriver driver;

    @FindBy(xpath = "//a[contains(@href,'/gist/')]")
    public List<WebElement> gistList;

    @FindBy(linkText = "Your gists")
    WebElement yourGistsLink;

    public GistListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openYourGists() {
        yourGistsLink.click();
    }

    public boolean isGistListDisplayed() {
        return gistList.size() > 0;
    }

    public void openFirstGist() {
        gistList.get(0).click();
    }
}
