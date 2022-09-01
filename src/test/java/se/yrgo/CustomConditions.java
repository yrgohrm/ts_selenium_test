package se.yrgo;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

public final class CustomConditions {
    private CustomConditions() {}

    public static ExpectedCondition<Boolean> elementHasBeenClicked(final WebElement element) {
        return ignoringAllExceptions(() -> element.click());
    }

    public static ExpectedCondition<Boolean> visibleTextHasBeenSelected(final Select element, final String text) {
        return ignoringAllExceptions(() -> element.selectByVisibleText(text));
    }

    private static ExpectedCondition<Boolean> ignoringAllExceptions(final Runnable func) {
        return driver -> {
            try {
                func.run();
                return true;
            }
            catch (Exception ex) {
                return false;
            }
        };
    }
}
