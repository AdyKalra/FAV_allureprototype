package com.amedia.qa.automation.testcases;

import com.amedia.qa.automation.db.DatabaseManager;
import com.amedia.qa.automation.webdriver.Framework;
import com.amedia.qa.automation.webdriver.PageInteraction;
import com.amedia.qa.automation.webdriver.WebServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.*;

import java.io.File;
import java.net.InetAddress;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by voravuthboonchai on 3/29/2016 AD.
 */
public class BaseTest {

    private static int countTestFailed;
    private static int countTestPassed;
    private static int countTestSkipped;
    private static int countTestRun;
    private String testSuiteName;
    private String testSuiteStartDate;
    private String testStatus;

    public Framework framework = new Framework();
    public PageInteraction page = new PageInteraction();
    public WebServices service = new WebServices();
    public DatabaseManager db = new DatabaseManager();

    Logger log = LoggerFactory.getLogger(BaseTest.class);

    public BaseTest() {

    }

    @BeforeSuite
    //Create test suite folder
    public void setupTestSuite(ITestContext context) {
        try {
            testSuiteStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            testSuiteName = context.getCurrentXmlTest().getSuite().getName().toString();

            framework.globalTestRunFolder = System.getProperty("user.dir") + "/testruns/" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "/"
                    + InetAddress.getLocalHost().getHostName() + "/" + testSuiteName + "_" + new SimpleDateFormat("HHmmssSSS").format(Calendar.getInstance().getTime()) + "/";

            TestRunner runner = (TestRunner) context;
            runner.setOutputDirectory(framework.globalTestRunFolder);

            log.info("Establishing test output directory at " + framework.globalTestRunFolder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterClass
    public void summarizeTestReults(ITestContext context) {
        countTestRun = countTestRun + context.getAllTestMethods().length;
        countTestPassed = countTestPassed + context.getPassedTests().size();
        countTestFailed = countTestFailed + context.getFailedTests().size();
        countTestSkipped = countTestSkipped + context.getSkippedTests().size();
    }

    @Parameters("recordTestRunsToDB")
    @AfterSuite
    public void recordTestResultsToDB(ITestContext context, boolean recordTestRunsToDB) {
        try {
            if (countTestFailed > 0) {
                testStatus = "Failed";
            } else if (countTestPassed == countTestRun) {
                testStatus = "Passed";
            } else if (countTestSkipped == countTestRun) {
                testStatus = "Skipped";
            }

            //copy logger.log to test run folder.
            Files.copy(new File(System.getProperty("user.dir") + "/logger.log").toPath(), new File(framework.globalTestRunFolder + "/logger.log").toPath());

            //To record test runs to database.
            if (recordTestRunsToDB == true) {
                db.recordTestExecutionResults(testSuiteName, testStatus, countTestRun, countTestPassed, countTestFailed, countTestSkipped, testSuiteStartDate, framework.globalTestRunFolder);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
