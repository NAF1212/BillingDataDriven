package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private WebDriver driver;
    private By emailField = By.id("inputEmail");
    private By passwordField = By.id("inputPassword");
    private By loginButton = By.id("loginButton");

    //Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }

    public void login(String email, String password) throws InterruptedException {
        enterEmail(email);
        enterPassword(password);
        Thread.sleep(2000);
        clickLoginButton();
    }
}