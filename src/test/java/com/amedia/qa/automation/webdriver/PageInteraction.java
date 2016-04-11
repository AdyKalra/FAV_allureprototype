package com.amedia.qa.automation.webdriver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

/**
 * Created by voravuthboonchai on 3/29/2016 AD.
 */
public class PageInteraction extends Framework {

    private int MAXIMUM_BUSY_WAIT = 60;
    private double STATIC_WAIT = 0.8;

    Logger log = LoggerFactory.getLogger(PageInteraction.class);

    public PageInteraction() {

    }

    //Find the web element for before perform any actions.
    private WebElement findElement(final By by) {
        try {
            List<WebElement> elements = driver.findElements(by);
            if (elements.size() > 0) {
                return elements.get(0);
            }

            return null;
        } catch (Exception ex) {
            new WebDriverWait(driver, DEFAULT_WAIT).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    try {
                        driver.findElements(by);
                        return Boolean.TRUE;
                    } catch (Exception ex) {
                        return Boolean.FALSE;
                    }
                }
            });
        }
        List<WebElement> elements = driver.findElements(by);
        if (elements.size() == 0) {
            return elements.get(0);
        }
        return null;
    }

    //Wait for the element present on the web page.
    private boolean waitForElementPresent(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT);
            //wait.until(ExpectedConditions.presenceOfElementLocated(by));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }

    //Wait for busy icon displayed
    private void waitForBusyIcon() {
        try {
            (new WebDriverWait(driver, 3)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver d) {
                    return isBusyIcon();
                }
            });
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

    //Wait for busy icon disappeared
    private void waitForNotBusyIcon() {
        (new WebDriverWait(driver, MAXIMUM_BUSY_WAIT)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return !isBusyIcon();
            }
        });
    }

    //Check if the busy icon is displayed or not
    protected Boolean isBusyIcon() {
        return isHourGlassCursorDisplayed();
    }

    //Check if the hour glass icon displayed on the screen or not
    protected Boolean isHourGlassCursorDisplayed() {
        return "wait".equals(executeJavascript("document.body.style.cursor"));
    }

    //To execute javascript
    protected Object executeJavascript(String javascript) {
        try {
            Object returnValue = ((JavascriptExecutor) driver).executeScript("return " + javascript);
            return returnValue;
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            return null;
        }
    }

    //To wait for static time.
    private void staticWait() {
        try{
            Thread.sleep((long) STATIC_WAIT * 1000);
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

    //Open the specific url.
    @Step("Navigate to url : {0}")
    public void openURL(String url) {
        driver.get(url);
        log.info("Navigate to URL : " + url);
    }

    //Type the specific text.
    @Step("Type in object [{0}] with value : {1}")
    public void type(By by, String value) {
        try {
            if (waitForElementPresent(by)) {
                clear(by);
                findElement(by).sendKeys(value);
                staticWait();
                waitForNotBusyIcon();

                log.info("Type in object [" + by + "] with value : " + value);
            } else {
                log.warn("Unable to find an element [" + by + "].");
                Assert.fail("Unable to find an element [" + by + "].");
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //Clear any value in the textbox before typing text.
    private void clear(By by) {
        try {
            waitForElementPresent(by);
            findElement(by).clear();
            findElement(by).sendKeys(Keys.chord(Keys.CONTROL, "a"));
            findElement(by).sendKeys(Keys.DELETE);
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

    //Click on any objects.
    @Step("Click at object [{0}]")
    public void click(By by) {
        try {
            if (waitForElementPresent(by)) {
                findElement(by).click();
                staticWait();
                waitForNotBusyIcon();

                log.info("Click at object [" + by + "]");
            } else {
                log.warn("Unable to find an element [" + by + "].");
                Assert.fail("Unable to find an element [" + by + "].");
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //Click on any objects.
    @Step("Click at link text : {0}")
    public void clickLinkText(String text) {
        try {
            if (waitForElementPresent(By.linkText(text))) {
                findElement(By.linkText(text)).click();
                staticWait();
                waitForNotBusyIcon();

                log.info("Click at link text : " + text);
            } else {
                log.warn("Unable to find an element with link text [" + text + "].");
                Assert.fail("Unable to find an element with link text [" + text + "].");
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //Check on checkbox.
    @Step("Check checkbox[{1}] at object [{0}]")
    public void check(By by, boolean value) {
        WebElement element;
        try {
            if (waitForElementPresent(by)) {
                element = findElement(by);
                if (value == true) {
                    if (!element.isSelected()) {
                        element.click();
                        staticWait();
                        waitForNotBusyIcon();
                    }
                } else {
                    if (element.isSelected()) {
                        element.click();
                        staticWait();
                        waitForNotBusyIcon();
                    }
                }
                log.info("Check checkbox [" + value + "] at object [" + by + "]");
            } else {
                log.warn("Unable to find an element [" + by + "].");
                Assert.fail("Unable to find an element [" + by + "].");
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //Select combo box.
    @Step("Select list box [{0}] with value : {1}")
    public void select(By by, String textSelected) {
        WebElement element;
        try {
            if (waitForElementPresent(by)) {
                element = findElement(by);
                if (element.isEnabled()) {
                    Select select = new Select(element);
                    select.selectByVisibleText(textSelected);
                    staticWait();
                    waitForNotBusyIcon();
                    log.info("Select list box with text : " + textSelected + " from object [" + by + "].");
                } else {
                    log.warn("The list box ["+ by +"] is not enabled. Please check.");
                    Assert.fail("The list box ["+ by +"] is not enabled. Please check.");
                }
            } else {
                log.warn("Unable to find an element [" + by + "].");
                Assert.fail("Unable to find an element [" + by + "].");
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //To verify the value of the element.
    @Step("Verify object [{0}] with value : {1}")
    public void verifyValue(By by, String expectedValue) {
        String actualValue;
        softAssert = new SoftAssert();
        try {
            if (waitForElementPresent(by)) {
                actualValue = findElement(by).getText().trim();
                
                softAssert.assertEquals(actualValue, expectedValue);
                if (actualValue.equals(expectedValue)) {
                    log.info("Verify value is matched with the expected value : " + expectedValue);

                } else {
                    log.warn("Verify value is not matched with the expected value : " + expectedValue + ", Actual value is " + actualValue);
                }
                assertAll();
            } else {
                log.warn("Unable to find an element [" + by + "].");
                Assert.fail("Unable to find an element [" + by + "].");
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

}
