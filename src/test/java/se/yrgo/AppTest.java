package se.yrgo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import se.yrgo.pos.*;

/**
 * Unit test for simple App.
 */
class AppTest {
    WebDriver driver;

    @BeforeEach
    void setupTest() {
        // Which driver do you want to use?
        // Selenium Manager will find and download the correct one for you

        driver = new ChromeDriver();
        // driver = new EdgeDriver();
        // driver = new FirefoxDriver();
        
        // try {
        //     ChromeOptions options = new ChromeOptions();
        //     driver = new RemoteWebDriver(new URL("http://localhost:4444"), options, false);
        // } catch (MalformedURLException e) {
        //     fail(e);
        // }
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    /**
     * The test written in a very simple way. Might fail due to a lot of things.
     * 
     */
    @Test
    void productQueryNavigationSimple() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://laromedel.nu/selenium");

        WebElement edLink = driver.findElement(By.linkText("Products"));
        edLink.click();

        Select edSelect = new Select(driver.findElement(By.id("unit")));
        edSelect.selectByVisibleText("Security");

        WebElement input = driver.findElement(By.id("query"));
        input.sendKeys("vulnerabilities");

        WebElement submit = driver.findElement(By.id("search"));
        submit.click();

        WebElement link = driver.findElement(By.linkText("SQL Injection - OWASP"));
        assertNotNull(link);

        WebElement resultsElement = driver.findElement(By.id("results"));
        assertFalse(resultsElement.getText().contains("business opportunity"));
    }

    /**
     * Improve the test with some waiting to make sure things work as intended.
     * Also maximize the window to have as much as possible in view.
     * 
     */
    @Test
    void productQueryNavigation() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        driver.get("https://laromedel.nu/selenium");

        final WebElement edLink = driver.findElement(By.linkText("Products"));

        var wait = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .ignoring(ElementClickInterceptedException.class);

        wait.until(d -> {
            edLink.click();
            return edLink;
        });

        var selectElement = driver.findElement(By.id("unit"));
        Select edSelect = new Select(selectElement);

        wait = wait.ignoring(ElementNotInteractableException.class);

        wait.until(d -> {
            edSelect.selectByVisibleText("Security");
            return edSelect;
        });

        WebElement input = driver.findElement(By.id("query"));
        input.sendKeys("vulnerabilities");

        WebElement submit = driver.findElement(By.id("search"));
        wait.until(d -> {
            submit.click();
            return edLink;
        });

        WebElement link = driver.findElement(By.linkText("SQL Injection - OWASP"));
        assertNotNull(link);

        WebElement resultsElement = driver.findElement(By.id("results"));
        assertFalse(resultsElement.getText().contains("business opportunity"));
    }

    /**
     * Improve the test with custom conditions to make the waiting look a bit
     * prettier.
     * 
     */
    @Test
    void productQueryNavigationSomewhatGoodLooking() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        driver.get("https://laromedel.nu/selenium");
        
        final var wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        final var edLink = Utils.find(driver, By.linkText("Products"));

        wait.until(CustomConditions.elementHasBeenClicked(edLink));

        Select edSelect = Utils.findSelect(driver, By.id("unit"));

        wait.until(CustomConditions.visibleTextHasBeenSelected(edSelect, "Security"));

        WebElement input = driver.findElement(By.id("query"));
        input.sendKeys("vulnerabilities");

        WebElement submit = driver.findElement(By.id("search"));
        wait.until(CustomConditions.elementHasBeenClicked(submit));

        WebElement link = driver.findElement(By.linkText("SQL Injection - OWASP"));
        assertNotNull(link);

        WebElement resultsElement = driver.findElement(By.id("results"));
        assertFalse(resultsElement.getText().contains("business opportunity"));
    }

    /**
     * Introduce page objects to let the test show intent, 
     * not implementation details.
     * 
     */
    @Test
    void productQueryNavigationPageObjects() {      
        StartPage startPage = Utils.openStartPage(driver);
        ProductsPage productPage = startPage.navigateToProducts();
        productPage.query("Security", "vunerabilities");
        
        assertNotNull(productPage.findLinkInResults("SQL Injection - OWASP"));
        assertFalse(productPage.getResultElement().getText().contains("business opportunity"));
    }
}
