import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SauceDemoTest {

    ChromeDriver driver;

    @BeforeMethod
    public void beforeTest() {
        driver = new ChromeDriver();
        driver.get("https://saucedemo.com");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.name("password")).sendKeys("secret_sauce");
        driver.findElement(By.xpath("//input[@data-test='login-button']")).click();
    }

    @Test
    public void verifyLoggedInTest() {
        String productsText = driver.findElement(By.className("title")).getText();
        Assertions.assertThat(productsText)
                .withFailMessage("Are u lalka? Expected title to be 'Products' but was '%s'", productsText)
                .isNotNull()
                .isNotEmpty()
                .startsWith("Prod")
                .endsWith("ucts")
                .isEqualTo("Products");
    }

    @AfterMethod()
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}