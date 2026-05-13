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

public class CheckoutOverviewTest {

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

        // Add to cart and navigate to Step Two
        wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))).click();
        driver.findElement(By.className("shopping_cart_link")).click();
        driver.findElement(By.id("checkout")).click();

        // Fill Step One details
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
    }

    // 63–66 - Verify Overview Page & Product Details
    @Test(priority = 1)
    public void verifyProductDetailsOnOverview() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-two"), "Overview page not loaded");

        String itemName = driver.findElement(By.className("inventory_item_name")).getText();
        String itemQty = driver.findElement(By.className("cart_quantity")).getText();
        String itemPrice = driver.findElement(By.className("inventory_item_price")).getText();

        Assert.assertEquals(itemName, "Sauce Labs Backpack", "Product name mismatch");
        Assert.assertEquals(itemQty, "1", "Quantity mismatch");
        Assert.assertTrue(itemPrice.contains("29.99"), "Price mismatch");
    }

    // 67,68 - Verify Payment and Shipping Info
    @Test(priority = 2)
    public void verifyOrderInfoLabels() {
        WebElement paymentInfo = driver.findElement(By.xpath("//div[text()='Payment Information:']"));
        WebElement shippingInfo = driver.findElement(By.xpath("//div[text()='Shipping Information:']"));

        Assert.assertTrue(paymentInfo.isDisplayed(), "Payment info label missing");
        Assert.assertTrue(shippingInfo.isDisplayed(), "Shipping info label missing");
    }

    // 69 - Verify Price Calculations
    @Test(priority = 3)
    public void verifyPriceCalculations() {
        String subtotalText = driver.findElement(By.className("summary_subtotal_label")).getText().replace("Item total: $", "");
        String taxText = driver.findElement(By.className("summary_tax_label")).getText().replace("Tax: $", "");
        String totalText = driver.findElement(By.className("summary_total_label")).getText().replace("Total: $", "");

        double subtotal = Double.parseDouble(subtotalText);
        double tax = Double.parseDouble(taxText);
        double total = Double.parseDouble(totalText);

        Assert.assertEquals(subtotal, 29.99, "Subtotal is incorrect");


        double expectedTotal = Math.round((subtotal + tax) * 100.0) / 100.0;
        Assert.assertEquals(total, expectedTotal, "Final total calculation mismatch");
    }

    // 70 - Verify Finish button works
    @Test(priority = 4)
    public void verifyFinishButton() {
        WebElement finishBtn = driver.findElement(By.id("finish"));
        finishBtn.click();

        wait.until(ExpectedConditions.urlContains("checkout-complete"));
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete"), "Failed to reach completion page");


        String header = driver.findElement(By.className("complete-header")).getText();
        Assert.assertEquals(header, "Thank you for your order!", "Order completion message mismatch");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}