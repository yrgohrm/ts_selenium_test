package se.yrgo.pos;

import java.time.*;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import se.yrgo.*;

public class ProductsPage {
    private WebDriver driver;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;

        if (!driver.getTitle().contains("Cool Company - Products")) {
            throw new IllegalStateException("Not on the correct page");
        }
    }

    public void query(String unit, String query) {
        final var wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        Select edSelect = Utils.findSelect(driver, By.id("unit"));

        wait.until(CustomConditions.visibleTextHasBeenSelected(edSelect, unit));

        WebElement input = driver.findElement(By.id("query"));
        input.sendKeys(query);

        WebElement submit = driver.findElement(By.id("search"));
        wait.until(CustomConditions.elementHasBeenClicked(submit));
    }
    
    public WebElement getResultElement() {
        return driver.findElement(By.id("results"));
    }

    public List<WebElement> findLinkInResults(String linkText) {
        final var wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement results = getResultElement();
        return wait.until(d -> results.findElements(By.linkText(linkText)));
    }
}
