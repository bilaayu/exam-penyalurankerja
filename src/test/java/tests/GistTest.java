package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.GistListPage;
import pages.GistPage;
import pages.LoginPage;
import utils.ConfigReader;

public class GistTest extends tests.BaseTest {

    @Test
    public void createPublicGistTest() {
        LoginPage login = new LoginPage(driver);
        GistPage gist = new GistPage(driver);

        login.login(ConfigReader.get("email"), ConfigReader.get("password"));

        // Klik tombol plus → pilih "New gist"
        gist.openNewGistDropdown();
        gist.clickNewGist();

        // Isi dan simpan gist baru
        gist.createSecretGist(
                "Automation Test",
                "test.txt",
                "Hello from automation"
        );
    }


    @Test
    public void editExistingGistTest() {
        LoginPage login = new LoginPage(driver);
        GistPage gist = new GistPage(driver);

        login.login(ConfigReader.get("email"), ConfigReader.get("password"));

        gist.openNewGistDropdown();
        gist.clickNewGist();
        gist.createSecretGist(
                "Automation Test",
                "test.txt",
                "Hello from automation"
        );

        gist.clickFirstEditButton();

        // edit description dan content
        gist.editFirstGist(
                "Edited Description",
                "Edited content from automation"
        );
    }


    @Test
    public void deleteExistingGistTest() {
        LoginPage login = new LoginPage(driver);
        GistListPage gistList = new GistListPage(driver);
        GistPage gist = new GistPage(driver);

        login.login(ConfigReader.get("email"), ConfigReader.get("password"));

        gist.openNewGistDropdown();
        gist.clickNewGist();
        gist.createSecretGist(
                "Automation Test",
                "test.txt",
                "Hello from automation"
        );

        gist.deleteFirstGist();
    }
}
