package placelab.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import placelab.utilities.WebDriverSetup;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeTest;

public class LoginTest {

    public WebDriver driver;
    private String host = System.getProperty("host");
    private String homePageUrl = "https://demo.placelab.com/dashboard/traffic";
    private String user = "Semir Hajdar";
    private String username = System.getProperty("username");
    private String password = System.getProperty("password");
    private String forgotPasswordUrl = "https://demo.placelab.com/password/forgot";
    private String forgotPasswordSecondUrl = "https://demo.placelab.com/email/sent";

    @Parameters({"browser"})

    @BeforeTest (alwaysRun = true, groups = {"Positive,Negative"},description = "Verify that user is able to open" + "PlaceLab App.")
    public void openApp(String browser) {


        driver = WebDriverSetup.getWebDriver(browser);
        driver.navigate().to(host);
        Assert.assertEquals(driver.getCurrentUrl(),host);
        Assert.assertEquals(driver.getTitle(),"PlaceLab");

        WebElement mainSubtitle = driver.findElement(By.xpath("/html/body/div/div[1]/div/p"));
        boolean subtitlePresent = mainSubtitle.isDisplayed();
        Assert.assertTrue(subtitlePresent);

        WebElement firstSubtitle = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[3]/div[1]/div[2]/p"));
        boolean firstSubtitlePresent = firstSubtitle.isDisplayed();
        Assert.assertTrue(firstSubtitlePresent);

        WebElement secondSubtitle = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[3]/div[2]/div[2]/p"));
        boolean secondSubtitlePresent = secondSubtitle.isDisplayed();
        Assert.assertTrue(secondSubtitlePresent);

        WebElement thirdSubtitle = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[3]/div[3]/div[2]/p"));
        boolean thirdSubtitlePresent = thirdSubtitle.isDisplayed();
        Assert.assertTrue(thirdSubtitlePresent);
    }
    @Test(priority = 3,groups = {"Positive"},description = "Verify that user is able to Log in" + "PlaceLab with valid credentials.", suiteName = "Smoke Test")
    public void testLoginPagePositive () {

        driver.navigate().to(host);

        WebElement enterUsername = driver.findElement(By.name("email"));
        enterUsername.sendKeys(username);

        WebElement enterPassword = driver.findElement(By.name("password"));
        enterPassword.sendKeys(password);

        WebElement submit = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/form/input[4]"));
        submit.click();

        Assert.assertEquals(driver.getCurrentUrl(), homePageUrl);
        try {
            WebElement userName = driver.findElement(By.id("user-name"));
            assert (userName.getText().contains(user));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Expected user is not logged in !");
        }

        WebElement userRole = driver.findElement(By.id("user-role"));
        Assert.assertEquals(userRole.getText(), "Group Admin");

        WebElement userMenu = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/ul[2]/li/a/div[1]"));
        userMenu.click();

        WebElement signOut = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div/div/ul[2]/li/ul/li[4]/a"));
        signOut.click();

        Assert.assertEquals(driver.getCurrentUrl(),host);

    }
    @Test(priority = 1,groups = {"Negative"},description = "Verify that user is not able to Log in with invalid username" + "PlaceLab with invalid credentials.")
    public void testLoginPageInvalidUsername() {

        driver.navigate().to(host);
        WebElement enterUsername = driver.findElement(By.name("email"));
        enterUsername.sendKeys("sssemirhajdar2@gmail.com");

        WebElement enterPassword = driver.findElement(By.name("password"));
        enterPassword.sendKeys(password);

        WebElement submit = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/form/input[4]"));
        submit.click();
    }
    @Test (priority = 2, groups = {"Negative"}, description = "Verify that user is nto able to Log in with invalid password" + "PlaceLab with invalid credentials.")
    public void testLoginPageInvalidPassword (){
        driver.navigate().to(host);

        WebElement enterUsername = driver.findElement(By.name("email"));
        enterUsername.sendKeys(username);

        WebElement enterPassword = driver.findElement(By.name("password"));
        enterPassword.sendKeys("password");

        WebElement submit = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/form/input[4]"));
        submit.click();
    }
    @Test(priority = 4, groups = {"Positive"},description = "Verify that user can reset password with 'Forget your password' functionality." + "PlaceLab with valid credentials.")
    public void testForgetYourPassword (){
        driver.navigate().to(host);

        WebElement forgotYourPassword = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/form/div[2]/a"));
        forgotYourPassword.click();

        Assert.assertEquals(driver.getCurrentUrl(),forgotPasswordUrl);

        WebElement resetEmail = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/form/div/input"));
        resetEmail.sendKeys(username);

        WebElement continueButton = driver.findElement(By.xpath("/html/body/div/div[1]/div/div[2]/form/input[3]"));
        continueButton.click();

        Assert.assertEquals(driver.getCurrentUrl(),forgotPasswordSecondUrl);

    }

    @AfterTest(dependsOnGroups = {"Negative"},description = "Verify that user is not Logged in.")
    public void failedLogin() {

        Assert.assertEquals(driver.getCurrentUrl(),host);
    }
    @AfterSuite (alwaysRun = true)
    public void quitDriver() {

        driver.close();
    }
}
