import lv.acodemy.page_object.CartPage;
import lv.acodemy.page_object.HeaderPage;
import lv.acodemy.page_object.InventoryPage;
import lv.acodemy.page_object.LoginPage;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SauceDemoTest {

    ChromeDriver driver;
    ChromeOptions options;

    LoginPage loginPage;
    InventoryPage inventoryPage;
    HeaderPage headerPage;
    CartPage cartPage;

    @BeforeMethod
    public void beforeTest() {
        options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

         options.setExperimentalOption("prefs", prefs);

        options.addArguments("--incognito");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        driver.get("https://saucedemo.com");

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        headerPage = new HeaderPage(driver);
        cartPage = new CartPage(driver);
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
        Assertions.assertThat(headerPage.getCartBadgeText()).isEqualTo("1");

        headerPage.getShoppingCartLink().click();
        Assertions.assertThat(cartPage.getCartItems().size()).isEqualTo(1);

        cartPage.getCheckoutButton().click();

        System.out.println("123");

    }

    @AfterMethod()
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}