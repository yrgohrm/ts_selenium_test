package se.yrgo;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.yrgo.pos.StartPage;

public final class Utils {
    private Utils() {}

    public static StartPage openStartPage(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://yrgo.se");
        return new StartPage(driver);
    }

    public static WebElement find(WebDriver driver, By locator) {
        final var wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return driver.findElement(locator);
    }

    public static Select findSelect(WebDriver driver, By locator) {
        return new Select(find(driver, locator));
    }
}
