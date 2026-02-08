package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GistPage {

    WebDriver driver;
    WebDriverWait wait;

    public GistPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        // Timeout diperpanjang supaya lebih aman
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // -------- Elements --------
    @FindBy(xpath = "//*[@id=\"_R_5jpb_\"]/span[1]")
    WebElement newGistDropdownBtn;

    @FindBy(xpath = "//span[text()='New gist']")
    WebElement newGistOption;

    @FindBy(name = "gist[description]")
    WebElement description;

    @FindBy(name = "gist[contents][][name]")
    WebElement fileName;

    @FindBy(xpath = "//*[@id='code-editor']")
    WebElement codeEditor;

    @FindBy(xpath = "//button[normalize-space(text())='Create secret gist']")
    WebElement saveBtn;

    // Ambil semua tombol Edit di list gist
    @FindBy(xpath = "//a[.//span[@class='Button-label' and text()='Edit']]")
    List<WebElement> editBtns;

    @FindBy(xpath = "//*[@id='gist-pjax-container']/div[1]/div/div[1]/ul[2]/li[2]/form/button")
    WebElement deleteBtn;

    @FindBy(xpath = "//button[contains(text(),'Delete this gist')]")
    WebElement confirmDeleteBtn;

    @FindBy(xpath = "//*[@id=\"gist-pjax-container\"]/div[1]/div/div/ul/li[5]/a[2]")
    WebElement yourGistsLink;

    @FindBy(xpath = "//a[@class='Link--muted' and contains(@href, '/qa26linemaker/')]")
    List<WebElement> gistLinks;

    @FindBy(xpath = "//*[@id=\"edit_gist_145265692\"]/div/div[2]/button")
    WebElement updateSecretGistBtn;

    // -------- Methods --------

    public void openNewGistDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(newGistDropdownBtn)).click();
    }

    public void clickNewGist() {
        wait.until(ExpectedConditions.elementToBeClickable(newGistOption)).click();
    }

    // Masukkan konten di CodeMirror editor
    public void enterContent(String text) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".CodeMirror")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "let cm = document.querySelector('.CodeMirror');" +
                        "cm.CodeMirror.setValue(arguments[0]);" +
                        "cm.CodeMirror.refresh();",
                text
        );

        // Tunggu tombol Save muncul dan clickable setelah isi CodeMirror
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Create secret gist')]")));
    }

    // Buat Gist publik baru
    public void createSecretGist(String desc, String file, String text) {
        wait.until(ExpectedConditions.visibilityOf(description)).sendKeys(desc);
        fileName.sendKeys(file);
        enterContent(text);
        clickSaveButton();
    }

    // Klik tombol Save aman pakai JS click
    public void clickSaveButton() {
        // scroll dulu supaya tombol visible
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveBtn);

        // tunggu tombol clickable sebelum klik
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
        button.click();
    }

    // Edit gist pertama di list
    public void editFirstGist(String newDesc, String newContent) {
        // 1. Update description
        wait.until(ExpectedConditions.visibilityOf(description));
        description.clear();
        description.sendKeys(newDesc);

        // 2. Update CodeMirror content via JS
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "document.querySelector('.CodeMirror').CodeMirror.setValue(arguments[0]);",
                newContent
        );

        // 3. Tunggu sebentar biar JS render selesai
        try { Thread.sleep(500); } catch (InterruptedException e) {}

        // 4. Scroll dan klik tombol Update via JS (bypass Selenium clickable issue)
        js.executeScript(
                "let btn = document.evaluate(\"//button[contains(normalize-space(),'Update secret gist')]\"," +
                        "document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                        "btn.scrollIntoView(true); btn.click();"
        );
    }

    // Delete gist pertama
    public void deleteFirstGist() {
        // klik tombol delete
        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='gist-pjax-container']/div[1]/div/div[1]/ul[2]/li[2]/form/button")));
        deleteBtn.click();

        // switch ke alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        System.out.println("Alert text: " + alert.getText()); // optional, cek teks alert
        alert.accept(); // klik OK / konfirmasi

        // alert sudah diklik, lanjut tunggu halaman refresh / elemen hilang
        wait.until(ExpectedConditions.invisibilityOf(deleteBtn));
    }

    public void openYourGists() {
        wait.until(ExpectedConditions.elementToBeClickable(yourGistsLink)).click();
    }

    public void clickFirstEditButton() {
        wait.until(ExpectedConditions.visibilityOfAllElements(editBtns));
        if (!editBtns.isEmpty()) {
            editBtns.get(0).click();
        } else {
            throw new RuntimeException("Tidak ada tombol Edit ditemukan");
        }
    }

    public void clickUpdateSecretGist() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // scroll dulu biar tombol visible
        js.executeScript("arguments[0].scrollIntoView(true);", updateSecretGistBtn);

        // klik pakai JS supaya pasti
        js.executeScript("arguments[0].click();", updateSecretGistBtn);
    }

}
