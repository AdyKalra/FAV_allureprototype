package com.amedia.qa.automation.testcases.testgoogle;

import com.amedia.qa.automation.pageobjects.google.GoogleMainPage;
import com.amedia.qa.automation.testcases.BaseTest;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;

/**
 * Created by voravuthboonchai on 3/29/2016 AD.
 */

@Features("Google")
@Stories({"BT-0001 : Search working properly"})

public class TestGoogle extends BaseTest {

    @Parameters("browser")
    @BeforeClass(description = "Setup test.")
    public void setupTest(@Optional("chrome") String browser) {
        framework.startDriver(browser);
    }

    @TestCaseId("TC001 : xxxxxxxxxx")
    @Test(priority = 1, description = "Navigate to google.com and search with ABC Tech keyword.")
    public void searchABCTechInGoogle() {
        page.openURL("https://www.google.com");
        page.type(GoogleMainPage.searchBox, "ABC TECH");
        page.click(GoogleMainPage.searchButton);
        page.clickLinkText("ABCTech thailand – We bring fun back to work");
        framework.captureImage();
    }

    @Test(priority = 2, description = "Click on the contact us link.")
    public void clickContactUs() {
        page.clickLinkText("Contact us");
    }

    @Test(priority = 3, description = "Input some data into text fields.")
    public void inputInfoInContactUs() {
        page.type(GoogleMainPage.name, "John Peter");
        page.type(GoogleMainPage.email, "John@Peter.com");
        page.type(GoogleMainPage.subject, "This is a test subject");
        page.type(GoogleMainPage.message, "This is a test message. It is just a test.");
    }

    @Test(priority = 4, description = "Verify text on page.")
    public void verifyText() {
        page.verifyValue(GoogleMainPage.emailLink, "contact@abctech-thailand.com");
        page.verifyValue(GoogleMainPage.howToReach, "How to reach as?");
        page.verifyValue(GoogleMainPage.bts, "BTS > Get off at BTS Asoke Station");
        framework.captureImage();
    }

    @Test(priority = 5, description = "Click on ABC logo to back to main page.")
    public void clickLogo() {
        page.verifyValue(GoogleMainPage.phone, "Phone\n+66 (0) 2663 4340");
        page.click(GoogleMainPage.abcLogo);
    }

    @AfterClass(description = "Stop test driver.")
    public void endingTest() {
        framework.stopDriver();
    }

}