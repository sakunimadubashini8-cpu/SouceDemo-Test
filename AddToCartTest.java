package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class AddToCartTest {

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
    }

    // 24,25,26 - Add to cart + verify button + cart count
    @Test(priority = 1)
    public void addSingleProduct() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // Re-finding to ensure no StaleElementReferenceException
        String buttonText = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
        Assert.assertEquals(buttonText, "Remove", "Button did not change to Remove");

        String cartCount = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartCount, "1", "Cart count incorrect");
    }

    // 27,28 - Add multiple products
    @Test(priority = 2)
    public void addMultipleProducts() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();

        String cartCount = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartCount, "2", "Cart count not updated correctly");
    }

    // 29,30,31 - Remove product
    @Test(priority = 3)
    public void removeProduct() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click(); // Add
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();      // Remove

        String buttonText = driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).getText();
        Assert.assertEquals(buttonText, "Add to cart", "Button did not revert");

        List<WebElement> badge = driver.findElements(By.className("shopping_cart_badge"));
        Assert.assertTrue(badge.isEmpty(), "Cart count badge should be removed");
    }

    // 32,33 - Add same product again
    @Test(priority = 4)
    public void addSameProductAgain() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click(); // Add
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();      // Remove
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click(); // Add again

        String cartCount = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartCount, "1", "Incorrect cart count");
    }

    // 34,35 - Cart icon clickable
    @Test(priority = 5)
    public void verifyCartIconClickable() {
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        Assert.assertTrue(cartIcon.isDisplayed(), "Cart icon not visible");

        cartIcon.click();
        Assert.assertTrue(driver.getCurrentUrl().contains("cart.html"), "Cart page not opened");
    }

    // 36 - Refresh retains cart
    @Test(priority = 6)
    public void verifyCartAfterRefresh() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.navigate().refresh();

        WebElement badge = driver.findElement(By.className("shopping_cart_badge"));
        Assert.assertEquals(badge.getText(), "1", "Cart lost after refresh");
    }

    // 37,38 - Add different products from different positions
    @Test(priority = 7)
    public void addDifferentProducts() {
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-fleece-jacket")).click();

        String cartCount = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartCount, "2", "Wrong items added");
    }

    // 39 - Multiple clicks stability
    @Test(priority = 8)
    public void multipleClicksStability() {
        // Toggle 4 times
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        List<WebElement> badge = driver.findElements(By.className("shopping_cart_badge"));
        Assert.assertTrue(badge.isEmpty(), "Badge should be gone after even number of clicks");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}