import lv.acodemy.page_object.InventoryPage;
import lv.acodemy.page_object.LoginPage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SauceDemoTest {

    ChromeDriver driver;
    LoginPage loginPage;
    InventoryPage inventoryPage;

    @BeforeMethod
    public void beforeTest() {
        driver = new ChromeDriver();
        driver.get("https://saucedemo.com");

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @Test
    public void verifyLoggedInTest() {
        loginPage.authorize("standard_user", "secret_sauce");

        String productsText = driver.findElement(By.className("title")).getText();
        Assertions.assertThat(productsText)
                .withFailMessage("Are u lalka? Expected title to be 'Products' but was '%s'", productsText)
                .isNotNull()
                .isNotEmpty()
                .startsWith("Prod")
                .endsWith("ucts")
                .isEqualTo("Products");
    }

    @Test
    public void logInTest() {
        loginPage.authorize("standard_user", "secret_sauce");
    }

    @Test
    public void addItemToTheCart() {
        loginPage.authorize("standard_user", "secret_sauce");
        inventoryPage.addItemToCartByName("Onesie");
    }

    @AfterMethod()
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}