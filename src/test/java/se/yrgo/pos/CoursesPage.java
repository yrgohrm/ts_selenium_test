package se.yrgo.pos;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import se.yrgo.CustomConditions;
import se.yrgo.Utils;

public class CoursesPage {
    private WebDriver driver;

    public CoursesPage(WebDriver driver) {
        this.driver = driver;

        if (!driver.getTitle().contains("Utbildningar")) {
            throw new IllegalStateException("Not on the correct page");
        }
    }


    public void selectSection(String section) {
        final var wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        Select edSelect = Utils.findSelect(driver, By.className("select"));

        wait.until(CustomConditions.visibleTextHasBeenSelected(edSelect, section));
    }
    
    public List<String> getCourseNames() {
        List<WebElement> h3s = driver.findElements(By.tagName("h3"));
        return h3s.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
