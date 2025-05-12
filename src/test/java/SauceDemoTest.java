import com.github.javafaker.Faker;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import lv.acodemy.page_object.*;
import lv.acodemy.utils.Constants;
import lv.acodemy.utils.VideoRecorderUtil;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Sauce Demo Test")
@Feature(value = "Add item to the cart")
@Listeners(AllureTestNg.class)
public class SauceDemoTest {

    ChromeDriver driver;
    WebDriverWait wait;
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
        options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://saucedemo.com");

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        headerPage = new HeaderPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test(testName = "Add item to the cart")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that item was added to the cart")
    public void addItemToTheCart() throws Exception {
//        VideoRecorderUtil.startRecording("addItemToTheCart");
        Allure.step("User is trying to authorize");
        loginPage.authorize("standard_user", "secret_sauce");

        Allure.step("Add item to the cart by name");
        inventoryPage.addItemToCartByName("Onesie");

        Allure.step("Check cart badge");
        assertThat(headerPage.getCartBadgeText()).isEqualTo("1");

        Allure.step("Click on shopping cart link");
        headerPage.getShoppingCartLink().click();
        assertThat(cartPage.getCartItems().size()).isEqualTo(1);

        Allure.step("Click on checkout button");
        wait.until(ExpectedConditions.elementToBeClickable(cartPage.getCheckoutButton()));
        cartPage.getCheckoutButton().click();

        String firstName = data.name().firstName();
        String lastName = data.name().lastName();
        String postalCode = data.address().zipCode();

        Allure.step("Fill checkout form");
        checkoutPage.fillCheckoutForm(
                firstName,
                lastName,
                postalCode);

        Allure.step("Complete purchase");
        checkoutPage.completePurchase();

        Allure.step("Verify that order was completed");
        assertThat(checkoutPage.getCompleteOrderHeader().getText())
                .isEqualTo(Constants.Messages.THANK_YOU_FOR_YOUR_ORDER);
        assertThat(checkoutPage.getCompleteOrderText().getText())
                .isEqualTo(Constants.Messages.ORDER_HAS_BEEN_DISPATCHED);

//        String recordedFilePath = VideoRecorderUtil.stopRecording();
//        InputStream videoStream = Files.newInputStream(Path.of(recordedFilePath));
//        Allure.addAttachment("Test Video", "video/mp4", videoStream, ".avi");
    }

    @AfterMethod()
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}