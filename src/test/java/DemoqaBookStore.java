import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class DemoqaBookStore {
    WebDriver driver;
    JavascriptExecutor js;

    WebElement loginButton;
    WebElement usernameField;
    WebElement passwordField;
    WebElement book1;
    WebElement addButton;
    WebElement book2;
    WebElement addButton1;
    List<WebElement> bookStoreApplicationElement;
    List<WebElement>profileButton;
    WebDriverWait wait;
    String validUsername,validPassword;


    @BeforeClass
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        validUsername = "JohnSmith";
        validPassword = "Johnsmith123@";

    }
    @BeforeMethod
    public void pageSetUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/");
        bookStoreApplicationElement = driver.findElements(By.cssSelector(".card.mt-4.top-card"));
        for(WebElement element : bookStoreApplicationElement){
            if(element.getText().equals("Book Store Application")){
                element.click();
                break;
            }
        }

        loginButton = driver.findElement(By.id("login"));
        loginButton.click();
        usernameField = driver.findElement(By.id("userName"));
        passwordField = driver.findElement(By.id("password"));
        loginButton = driver.findElement(By.id("login"));
        usernameField.sendKeys(validUsername);
        passwordField.sendKeys(validPassword);
        loginButton.click();
    }

    @Test(priority = 10)
    public void userCanAddBooks(){
        book1 = driver.findElement(By.linkText("Git Pocket Guide"));
        book1.click();
        addButton = driver.findElement(By.cssSelector(".text-right.fullButton"));
        js.executeScript("window.scrollBy(0,500)");
        addButton.click();
        driver.navigate().back();
        book2 = driver.findElement(By.linkText("Learning JavaScript Design Patterns"));
        book2.click();
        addButton1 = driver.findElement(By.cssSelector(".text-right.fullButton"));
        js.executeScript("window.scrollBy(0,500)");
        addButton1.click();
    }
    @Test(priority = 20)
    public void userCanLogoutWithCookies(){
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
    }
    @Test(priority = 30)
    public void afterLogOutBooksAreStillPresent() {
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.linkText("Learning JavaScript Design Patterns"))));
        profileButton = driver.findElements(By.cssSelector(".btn.btn-light "));
        for (WebElement element : profileButton){
            if(element.getText().equals("Profile")){
                js.executeScript("window.scrollBy(0,500)");
                element.click();
                break;
            }
        }
        WebElement book1AfterRelogin = driver.findElement(By.linkText("Git Pocket Guide"));
        WebElement book2AfterRelogin = driver.findElement(By.linkText("Learning JavaScript Design Patterns"));

        Assert.assertTrue(book1AfterRelogin.isDisplayed());
        Assert.assertTrue(book2AfterRelogin.isDisplayed());

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}
