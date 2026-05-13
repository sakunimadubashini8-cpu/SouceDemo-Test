package TestNG;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

    public class LoginTest {

        WebDriver driver;
        String baseURL = "https://www.saucedemo.com/";

        // Setup
        @BeforeMethod
        public void setUp() {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get(baseURL);
        }

        // Data Provider (Multiple Test Cases)
        @DataProvider(name = "loginData")
        public Object[][] loginDataProvider() {
            return new Object[][]{

                    // TC001 - Valid Login
                    {"standard_user", "secret_sauce"},
                    {"problem_user", "secret_sauce"},
                    {"performance_glitch_user", "secret_sauce"},
                    {"error_user", "secret_sauce"},
                    {"visual_user", "secret_sauce"},

                    // TC002 - Invalid Username
                    {"wrong_user", "secret_sauce"},

                    // TC003 - Invalid Password
                    {"standard_user", "wrong_pass"},

                    // TC004 - Both Invalid
                    {"abc", "123"},

                    // TC005 - Empty Username
                    {"", "secret_sauce"},

                    // TC006 - Empty Password
                    {"standard_user", ""},

                    // TC007 - Both Empty
                    {"", ""},

                    // TC008 - Locked User
                    {"locked_out_user", "secret_sauce"},


            };
        }

        // Test Method
        @Test(dataProvider = "loginData")
        public void testLogin(String username, String password) throws InterruptedException {

            Thread.sleep(2000);

            driver.findElement(By.id("user-name")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("login-button")).click();

            Thread.sleep(2000);

            System.out.println("Test executed with Username: " + username + " | Password: " + password);

            // Check result
            if (password.equals("secret_sauce") &&
                    (username.equals("standard_user") ||
                            username.equals("problem_user") ||
                            username.equals("performance_glitch_user") ||
                            username.equals("error_user") ||
                            username.equals("visual_user"))) {

                boolean isLoggedIn = driver.getCurrentUrl().contains("inventory.html");
                System.out.println("Expected: Login Success | Actual: " + (isLoggedIn ? "PASS" : "FAIL"));

            } else if (username.equals("locked_out_user")) {

                String errorMsg = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();
                System.out.println("Expected: Locked User Error | Actual: " + errorMsg);

            } else {

                boolean errorDisplayed = driver.findElements(By.cssSelector("h3[data-test='error']")).size() > 0;
                System.out.println("Expected: Login Fail | Actual: " + (errorDisplayed ? "PASS" : "FAIL"));
            }
        }

        // Tear Down
        @AfterMethod
        public void tearDown() throws InterruptedException {
            Thread.sleep(2000);
            if (driver != null) {
                driver.quit();
            }
        }
    }

