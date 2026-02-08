// GistListPage.java
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class GistListPage {

    WebDriver driver;
    WebDriverWait wait;

    public GistListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[@id=\"gist-pjax-container\"]/div[1]/div/div/ul/li[5]/a[2]")
    WebElement yourGistsLink;

    public void openYourGists() {
        wait.until(ExpectedConditions.elementToBeClickable(yourGistsLink)).click();
    }
}
