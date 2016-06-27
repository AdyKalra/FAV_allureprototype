package com.amedia.qa.automation.testcases.testgoogle;

import com.amedia.qa.automation.pageobjects.google.GoogleMainPage;
import com.amedia.qa.automation.testcases.BaseTest;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;

@Features("Google")
@Stories({"BT-0002 : Search working properly with Amedia"})
public class AmediaGoogle extends BaseTest {

    @Parameters("browser")
    @BeforeClass(description = "Start extent report.")
    public void setupTest(@Optional("chrome") String browser) {
        framework.startDriver(browser);
    }

    @Test(priority = 1, description = "Navigate to google.com and search with ABC Tech keyword.")
    public void searchAmediaInGoogle() {
        page.openURL("https://www.google.com");
        page.type(GoogleMainPage.searchBox, "amedia");
        page.click(GoogleMainPage.searchButton);
        page.clickLinkText("Amedia");
    }

    @Test(priority = 2, description = "Click on the contact us link.")
    public void clickChangeLanguageToEng() {
        page.clickLinkText("EN");
        framework.captureImage();
        page.verifyValue(GoogleMainPage.ceoName, "ARE STOKSTAD");
    }

    @AfterClass(description = "Stop test driver.")
    public void endingTest() {
        framework.stopDriver();
    }
}