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

public class OrderCompletionTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        // Initialize Explicit Wait with a 10-second timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Add an item to the cart and navigate to the Checkout Overview page
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        // Fill in required Checkout Information
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
    }

    // 71, 72, 73 -Click Finish and Verify Order Success Messages
    @Test(priority = 1)
    public void verifyOrderSuccessMessages() {
        //
        WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("finish")));
        finishBtn.click();

        // Verify that the URL changes to the checkout completion page
        wait.until(ExpectedConditions.urlContains("checkout-complete"));
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete"), "Success page not loaded");

        // Verify the "Thank You" header message
        String thankYouHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header"))).getText();
        Assert.assertEquals(thankYouHeader, "Thank you for your order!", "Thank You message mismatch");

        // Verify the order dispatch description text
        String completeText = driver.findElement(By.className("complete-text")).getText();
        Assert.assertTrue(completeText.contains("Your order has been dispatched"), "Completion text mismatch");
    }

    // 74, 75 - Click Back Home and Verify Navigation to Product Page
    @Test(priority = 2)
    public void verifyBackHomeNavigation() {
        // Navigate to the completion page first by clicking Finish
        WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("finish")));
        finishBtn.click();

        // 74. Click the "Back Home" button
        WebElement backHomeBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("back-to-products")));
        backHomeBtn.click();

        // 75. Verify return to the Inventory/Product page
        wait.until(ExpectedConditions.urlContains("inventory"));
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "Did not return to product page");

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
        Assert.assertEquals(title.getText(), "Products", "Page title mismatch after returning home");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}