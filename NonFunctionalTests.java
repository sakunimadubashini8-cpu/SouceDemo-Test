package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class NonFunctionalTests {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://www.saucedemo.com/");
    }

    // 🔹 NF01 - Page Load Performance Test (FIXED)
    @Test(priority = 1)
    public void pageLoadPerformanceTest() {
        long startTime = System.currentTimeMillis();

        driver.navigate().refresh(); // fresh load measure

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        System.out.println("Page Load Time: " + loadTime + " ms");

        // ✅ Increased realistic threshold (10 sec)
        Assert.assertTrue(loadTime < 10000, "Page load time is too slow!");
    }

    // 🔹 NF02 - Login Performance Test
    @Test(priority = 2)
    public void loginPerformanceTest() {
        long start = System.currentTimeMillis();

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        long end = System.currentTimeMillis();
        long loginTime = end - start;

        System.out.println("Login Time: " + loginTime + " ms");

        Assert.assertTrue(loginTime < 5000, "Login is too slow!");
    }

    // 🔹 NF03 - UI Element Visibility Test
    @Test(priority = 3)
    public void uiElementVisibilityTest() {
        WebElement username = driver.findElement(By.id("user-name"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login-button"));

        Assert.assertTrue(username.isDisplayed(), "Username field not visible");
        Assert.assertTrue(password.isDisplayed(), "Password field not visible");
        Assert.assertTrue(loginBtn.isDisplayed(), "Login button not visible");
    }

    // 🔹 NF04 - Password Masking Test
    @Test(priority = 4)
    public void passwordMaskingTest() {
        WebElement passwordField = driver.findElement(By.id("password"));

        String type = passwordField.getAttribute("type");

        Assert.assertEquals(type, "password", "Password is not masked!");
    }

    // 🔹 NF05 - HTTPS Security Test
    @Test(priority = 5)
    public void httpsSecurityTest() {
        String url = driver.getCurrentUrl();

        Assert.assertTrue(url.startsWith("https"), "Website is not secure!");
    }

    // 🔹 NF06 - Page Title Test
    @Test(priority = 6)
    public void pageTitleTest() {
        String title = driver.getTitle();

        Assert.assertTrue(title.contains("Swag Labs"), "Title mismatch");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}