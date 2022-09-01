package se.yrgo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import se.yrgo.pos.CoursesPage;
import se.yrgo.pos.StartPage;

/**
 * Unit test for simple App.
 */
public class AppTest {
    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        // Which driver do you want to use?
        
        WebDriverManager.chromedriver().setup();
        // WebDriverManager.edgedriver().setup();
        // WebDriverManager.firefoxdriver().setup();
    }

    @BeforeEach
    void setupTest() {
        // Which driver do you want to use?

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
    void educationNavigationSimple() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://yrgo.se");

        WebElement edLink = driver.findElement(By.linkText("Till utbildningarna"));
        edLink.click();

        Select edSelect = new Select(driver.findElement(By.className("select")));
        edSelect.selectByVisibleText("IT och teknik");

        List<WebElement> h3s = driver.findElements(By.tagName("h3"));
        assertTrue(h3s.stream().map(WebElement::getText)
                .anyMatch(txt -> txt.contains("Java Enterprise Utvecklare")));

        assertFalse(h3s.stream().map(WebElement::getText)
                .anyMatch(str -> str.contains("Apotekstekniker")));
    }

    /**
     * Improve the test with some waiting to make sure things work as intended.
     * Also maximize the window to have as much as possible in view.
     * 
     */
    @Test
    void educationNavigation() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        driver.get("https://yrgo.se");

        final WebElement edLink = driver.findElement(By.linkText("Till utbildningarna"));

        var wait = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .ignoring(ElementClickInterceptedException.class);

        wait.until(driver -> {
            edLink.click();
            return edLink;
        });

        var selectElement = driver.findElement(By.className("select"));
        Select edSelect = new Select(selectElement);

        wait = wait.ignoring(ElementNotInteractableException.class);

        wait.until(driver -> {
            edSelect.selectByVisibleText("IT och teknik");
            return edSelect;
        });

        List<WebElement> h3s = driver.findElements(By.tagName("h3"));
        assertTrue(h3s.stream().map(WebElement::getText)
                .anyMatch(str -> str.contains("Java Enterprise Utvecklare")));

        assertFalse(h3s.stream().map(WebElement::getText)
                .anyMatch(str -> str.contains("Apotekstekniker")));
    }

    /**
     * Improve the test with custom conditions to make the waiting look a bit
     * prettier.
     * 
     */
    @Test
    void educationNavigationSomewhatGoodLooking() {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        driver.get("https://yrgo.se");
        
        final var wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        final var edLink = Utils.find(driver, By.linkText("Till utbildningarna"));

        wait.until(CustomConditions.elementHasBeenClicked(edLink));

        Select edSelect = Utils.findSelect(driver, By.className("select"));

        wait.until(CustomConditions.visibleTextHasBeenSelected(edSelect, "IT och teknik"));

        List<WebElement> h3s = driver.findElements(By.tagName("h3"));
        assertTrue(h3s.stream().map(WebElement::getText)
                .anyMatch(str -> str.contains("Java Enterprise Utvecklare")));

        assertFalse(h3s.stream().map(WebElement::getText)
                .anyMatch(str -> str.contains("Apotekstekniker")));
    }

    /**
     * Introduce page objects to let the test show intent, 
     * not implementation details.
     * 
     */
    @Test
    void educationNavigationPageObjects() {      
        StartPage startPage = Utils.openStartPage(driver);
        CoursesPage coursesPage = startPage.navigateToCourses();
        coursesPage.selectSection("IT och teknik");

        var courseNames = coursesPage.getCourseNames();
        assertTrue(courseNames.contains("Java Enterprise Utvecklare"));
        assertFalse(courseNames.contains("Apotekstekniker"));
    }
}
