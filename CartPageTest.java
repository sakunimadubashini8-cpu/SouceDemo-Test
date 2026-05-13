package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class CartPageTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://www.saucedemo.com/");

        // Login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Add item before going to cart (needed for validations)
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
    }

    // 40,41 - Navigate to cart & verify page
    @Test(priority = 1)
    public void verifyCartNavigation() {
        driver.findElement(By.className("shopping_cart_link")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("cart"), "Did not navigate to cart page");

        String title = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(title, "Your Cart", "Cart title incorrect");
    }

    // 42-46 - Product validations
    @Test(priority = 2)
    public void verifyCartItemDetails() {
        driver.findElement(By.className("shopping_cart_link")).click();

        WebElement item = driver.findElement(By.className("cart_item"));

        String name = item.findElement(By.className("inventory_item_name")).getText();
        String desc = item.findElement(By.className("inventory_item_desc")).getText();
        String price = item.findElement(By.className("inventory_item_price")).getText();
        String qty = item.findElement(By.className("cart_quantity")).getText();

        Assert.assertEquals(name, "Sauce Labs Backpack");
        Assert.assertFalse(desc.isEmpty(), "Description missing");
        Assert.assertEquals(price, "$29.99");
        Assert.assertEquals(qty, "1");
    }

    // 47,48,49 - Remove item & empty cart
    @Test(priority = 3)
    public void verifyRemoveItem() {
        driver.findElement(By.className("shopping_cart_link")).click();

        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        List<WebElement> items = driver.findElements(By.className("cart_item"));
        Assert.assertTrue(items.size() == 0, "Cart not empty after removal");
    }

    // 50 - Continue Shopping
    @Test(priority = 4)
    public void verifyContinueShopping() {
        driver.findElement(By.className("shopping_cart_link")).click();

        driver.findElement(By.id("continue-shopping")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "Continue Shopping failed");
    }

    // 51 - Checkout button visible
    @Test(priority = 5)
    public void verifyCheckoutButton() {
        driver.findElement(By.className("shopping_cart_link")).click();

        WebElement checkoutBtn = driver.findElement(By.id("checkout"));
        Assert.assertTrue(checkoutBtn.isDisplayed(), "Checkout button not visible");
    }

    // 52 - Click checkout
    @Test(priority = 6)
    public void verifyCheckoutNavigation() {
        driver.findElement(By.className("shopping_cart_link")).click();

        driver.findElement(By.id("checkout")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"), "Did not navigate to checkout");
    }

    // 53 - Cart retains items before checkout
    @Test(priority = 7)
    public void verifyCartRetainsItems() {
        driver.findElement(By.className("shopping_cart_link")).click();

        List<WebElement> items = driver.findElements(By.className("cart_item"));
        Assert.assertTrue(items.size() > 0, "Items missing in cart");
    }

    // 54 - No wrong items appear
    @Test(priority = 8)
    public void verifyNoWrongItems() {
        driver.findElement(By.className("shopping_cart_link")).click();

        List<WebElement> items = driver.findElements(By.className("cart_item"));

        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            Assert.assertEquals(name, "Sauce Labs Backpack", "Unexpected item found");
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}