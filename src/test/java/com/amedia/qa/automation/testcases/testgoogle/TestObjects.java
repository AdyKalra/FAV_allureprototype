package com.amedia.qa.automation.testcases.testgoogle;

import com.amedia.qa.automation.pageobjects.google.ObjectsETC;
import com.amedia.qa.automation.testcases.BaseTest;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.TestCaseId;

/**
 * Created by voravuthboonchai on 4/11/2016 AD.
 */

@Features("Google")
@Stories({"BT-0001 : Search working properly"})

public class TestObjects extends BaseTest {

    @Parameters("browser")
    @BeforeClass(description = "Setup test.")
    public void setupTest(@Optional("chrome") String browser) {
        framework.startDriver(browser);
    }

    @TestCaseId("TC001 : xxxxxxxxxx")
    @Test(priority = 1, description = "Check ostiarius adding user page.")
    public void ostiariusAddUserWithCancel() {
        page.openURL("http://admin.snap0.api.no/singhaclient/app/ostiarius?token=SEc8YBR5UhtpKl4Ya05kfDEfBzY_SiMM");
        page.clickLinkText("Add User");
        page.type(ObjectsETC.googleUsername, "aaa@google.aa.com");
        page.type(ObjectsETC.firstName, "John");
        page.type(ObjectsETC.lastName, "Reeves");
        page.type(ObjectsETC.email, "aaa@google.bb.com");
        page.type(ObjectsETC.description, "This is a test.");
        page.select(ObjectsETC.primaryPublication, "Finnmarken");
        page.check(ObjectsETC.multidesk, true);
        framework.captureImage();
        page.click(ObjectsETC.cancelButton);
        framework.captureImage();
    }

    @TestCaseId("TC002 : yyyyyyyyyyyy")
    @Test(priority = 2, description = "Check transition scheduler.")
    public void transitionSetScheduler() {
        page.openURL("http://bearing.dev.abctech-thailand.com/transition/scheduler_admin.html");
        page.select(ObjectsETC.clientName, "adima");
        page.type(ObjectsETC.url, "www.aaa.com");
        page.type(ObjectsETC.parameter, "xxxxxx");
        page.click(ObjectsETC.minute_Choose);
        page.select(ObjectsETC.minute, "10");
        page.click(ObjectsETC.hour_Every);
        page.click(ObjectsETC.day_Every);
        page.click(ObjectsETC.month_Choose);
        page.select(ObjectsETC.month, "December");
        page.click(ObjectsETC.weekday_Choose);
        page.select(ObjectsETC.weekday, "Saturday");
        framework.captureImage();
    }

    @AfterClass(description = "Stop test driver.")
    public void endingTest() {
        framework.stopDriver();
    }

}