package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class CheckoutInformationTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Add an item to cart
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();

        // Go to cart
        driver.findElement(By.className("shopping_cart_link")).click();

        // 55. Checkout
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
    }

    // 56-59 Submit valid details
    @Test
    public void submitValidDetails() {
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))).click();

        // Wait for next page
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two"));
    }

    // 60. Empty fields
    @Test
    public void submitEmptyFields() {
        driver.findElement(By.id("continue")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(error.getText().contains("First Name is required"));
    }

    // 61. Missing first name
    @Test
    public void missingFirstNameValidation() {
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");

        driver.findElement(By.id("continue")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(error.getText().contains("First Name is required"));
    }

    // 62. Missing postal code
    @Test
    public void missingPostalCodeValidation() {
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");

        driver.findElement(By.id("continue")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(error.getText().contains("Postal Code is required"));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}