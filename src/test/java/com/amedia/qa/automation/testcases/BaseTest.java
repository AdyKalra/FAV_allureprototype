package com.amedia.qa.automation.testcases;

import com.amedia.qa.automation.db.DatabaseManager;
import com.amedia.qa.automation.webdriver.Framework;
import com.amedia.qa.automation.webdriver.PageInteraction;
import com.amedia.qa.automation.webdriver.WebServices;

import org.testng.ITestContext;
import org.testng.TestRunner;
import org.testng.annotations.*;

import java.net.InetAddress;
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

            System.out.println("Output test suite folder location : " + framework.globalTestRunFolder);
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
        if (countTestFailed > 0) {
            testStatus = "Failed";
        } else if (countTestPassed == countTestRun) {
            testStatus = "Passed";
        } else if (countTestSkipped == countTestRun) {
            testStatus = "Skipped";
        }

        //To record test runs to database.
        if (recordTestRunsToDB == true) {
            db.recordTestExecutionResults(testSuiteName, testStatus, countTestRun, countTestPassed, countTestFailed, countTestSkipped, testSuiteStartDate, framework.globalTestRunFolder);
            //copytestrunfoldertocentralize
        }
    }
}
