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

public class NegativeTests {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }

    // 76. Checkout with empty cart
    @Test(priority = 1)
    public void checkoutWithEmptyCart() {
        driver.findElement(By.className("shopping_cart_link")).click();

        WebElement checkoutBtn = driver.findElement(By.id("checkout"));
        checkoutBtn.click();


        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"),
                "System allowed checkout with empty cart (Actual site behavior)");
    }

    // 77. Remove item then checkout
    @Test(priority = 2)
    public void removeProductAndTryCheckout() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        driver.findElement(By.id("checkout")).click();


        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"),
                "System allowed checkout after removal (Actual site behavior)");
    }

    // 78. Skip entering checkout details
    @Test(priority = 3)
    public void skipAllDetails() {
        addItemAndGoToCheckout();
        driver.findElement(By.id("continue")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container.error")));
        Assert.assertTrue(error.getText().contains("First Name is required"), "Error message not shown");
    }

    // 79. Enter only first name
    @Test(priority = 4)
    public void enterOnlyFirstName() {
        addItemAndGoToCheckout();
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("continue")).click();

        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container.error")));
        Assert.assertTrue(error.getText().contains("Last Name is required"), "Error message not shown");
    }

    private void addItemAndGoToCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}