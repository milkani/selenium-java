import com.github.javafaker.Faker;
import lv.acodemy.page_object.*;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SauceDemoTest {

    ChromeDriver driver;
    ChromeOptions options;

    LoginPage loginPage;
    InventoryPage inventoryPage;
    HeaderPage headerPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    private static final Logger logger = LoggerFactory.getLogger(SauceDemoTest.class);

    Faker data = new Faker();

    @BeforeMethod
    public void beforeTest() {
        options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

         options.setExperimentalOption("prefs", prefs);

        options.addArguments("--incognito");
        options.addArguments("--start-maximized");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.get("https://saucedemo.com");

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        headerPage = new HeaderPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test(enabled = false)
    public void verifyLoggedInTest() {
        loginPage.authorize("standard_user", "secret_sauce");

        String productsText = driver.findElement(By.className("title")).getText();
        assertThat(productsText)
                .withFailMessage("Are u lalka? Expected title to be 'Products' but was '%s'", productsText)
                .isNotNull()
                .isNotEmpty()
                .startsWith("Prod")
                .endsWith("ucts")
                .isEqualTo("Products");
    }

    @Test(enabled = false)
    public void logInTest() {
        loginPage.authorize("standard_user", "secret_sauce");
    }

    @Test(testName = "Add item to the cart")
    public void addItemToTheCart() {
        logger.info("Scenario: Add item to the cart");

        logger.info("Step 1: User is trying to authorize");
        loginPage.authorize("standard_user", "secret_sauce");
        logger.info("User is authorized");

        logger.info("Step 2: Adding item to the cart");
        inventoryPage.addItemToCartByName("Onesie");
        logger.info("Item added to the cart");

        logger.info("Step 3: Checking if item was added to the cart");
        assertThat(headerPage.getCartBadgeText()).isEqualTo("1");
        logger.info("Item was added to the cart");

        headerPage.getShoppingCartLink().click();
        assertThat(cartPage.getCartItems().size()).isEqualTo(1);

        cartPage.getCheckoutButton().click();

        String firstName = data.name().firstName();
        String lastName = data.name().lastName();
        String postalCode = data.address().zipCode();

        logger.debug("First name: {}, last name: {}, postal code: {}", firstName, lastName, postalCode);
        checkoutPage.fillCheckoutForm(
                firstName,
                lastName,
                postalCode);

    }

    @AfterMethod()
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}