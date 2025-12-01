package tasks;

import pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class LoginTask {

    private LoginPage loginPage;

    public LoginTask(WebDriver driver) {
        this.loginPage = new LoginPage(driver);
    }

    public void login(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();
    }
}
