package se.yrgo.pos;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.yrgo.CustomConditions;
import se.yrgo.Utils;

public class StartPage {
    private WebDriver driver;

    public StartPage(WebDriver driver) {
        this.driver = driver;

        if (!driver.getTitle().equals("Cool Company - Start")) {
            throw new IllegalStateException("Not on the correct page");
        }
    }

    public ProductsPage navigateToProducts() {
        final var wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        final var edLink = Utils.find(driver, By.linkText("Products"));

        wait.until(CustomConditions.elementHasBeenClicked(edLink));

        return new ProductsPage(driver);
    }
}
