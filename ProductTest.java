package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class ProductTest {

    WebDriver driver;
    String baseURL = "https://www.saucedemo.com/";

    // 🔹 Setup
    @BeforeMethod
    public void setUp() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(baseURL);

        // Login before each test
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }

    // 🔹 9,10 Product Page Validation
    @Test(priority = 1)
    public void validateProductPage() {

        String currentURL = driver.getCurrentUrl();
        Assert.assertTrue(currentURL.contains("inventory"), "Product page not loaded");

        String title = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(title, "Products");
    }

    // 🔹 11,12. Menu Open / Close
    @Test(priority = 2)
    public void menuValidation() throws InterruptedException {

        driver.findElement(By.id("react-burger-menu-btn")).click();
        Thread.sleep(1000);

        boolean menuVisible = driver.findElement(By.id("react-burger-cross-btn")).isDisplayed();
        Assert.assertTrue(menuVisible, "Menu not opened");

        driver.findElement(By.id("react-burger-cross-btn")).click();
    }

    // 🔹 13. Product Image Check
    @Test(priority = 3)
    public void productImageValidation() {

        boolean imageDisplayed = driver.findElement(By.className("inventory_item_img")).isDisplayed();
        Assert.assertTrue(imageDisplayed, "Product image not visible");
    }

    // 🔹 14. Title Check
    @Test(priority = 4)
    public void productTitleValidation() {

        String productName = driver.findElement(By.className("inventory_item_name")).getText();
        Assert.assertFalse(productName.isEmpty(), "Product title is empty");
    }

    // 🔹 15. Description Check
    @Test(priority = 5)
    public void productDescriptionValidation() {

        String desc = driver.findElement(By.className("inventory_item_desc")).getText();
        Assert.assertFalse(desc.isEmpty(), "Description is empty");
    }

    // 🔹 16. Price + Currency Validation
    @Test(priority = 6)
    public void priceValidation() {

        String price = driver.findElement(By.className("inventory_item_price")).getText();

        Assert.assertTrue(price.contains("$"), "Currency symbol missing");
        Assert.assertTrue(price.length() > 1, "Price not valid");
    }

    // 🔹 17. Button Text Validation
    @Test(priority = 7)
    public void buttonTextValidation() {

        String buttonText = driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).getText();
        Assert.assertEquals(buttonText, "Add to cart");
    }

    // 🔹 18,19,20,21. Cart Operations
    @Test(priority = 8)
    public void cartOperationTest() {

        // Add to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        String cartCount = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartCount, "1");

        // Remove from cart
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        boolean cartEmpty = driver.findElements(By.className("shopping_cart_badge")).size() == 0;
        Assert.assertTrue(cartEmpty, "Cart not empty after removal");
    }

    // 🔹 22,23. Navigation (Product → Cart → Back)
    @Test(priority = 9)
    public void navigationTest() {

        driver.findElement(By.className("shopping_cart_link")).click();

        String cartURL = driver.getCurrentUrl();
        Assert.assertTrue(cartURL.contains("cart"), "Cart page not opened");

        driver.navigate().back();

        String backURL = driver.getCurrentUrl();
        Assert.assertTrue(backURL.contains("inventory"), "Navigation back failed");
    }

    // 🔹 Tear Down
    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        if (driver != null) {
            driver.quit();
        }
    }
}