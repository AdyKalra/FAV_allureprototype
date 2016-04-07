package com.amedia.qa.automation.webdriver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by voravuthboonchai on 3/29/2016 AD.
 */
public class Framework {

    public static WebDriver driver;
    public static int DEFAULT_WAIT = 15;

    //Set default location of logback configuration file
    static {System.setProperty("logback.configurationFile", System.getProperty("user.dir") + "/src/test/resources/logback.xml");}
    Logger log = LoggerFactory.getLogger(Framework.class);
    public static SoftAssert softAssert;

    public static String globalTestRunFolder;

    public Framework() {

    }

    // To start the web driver.
    @Step("Starting {0} driver.")
    public void startDriver(String browser) {
        String chromeDriver;
        try {
            switch(browser.toLowerCase()) {
                case "chrome":
                    if(System.getProperty("os.name").toLowerCase().contains("mac")) {
                        chromeDriver = "chromedriver_mac32";
                    } else if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
                        chromeDriver = "chromedriver_linux64";
                    } else {
                        chromeDriver = "chromedriver_mac32";
                    }

                    File files = new File(System.getProperty("user.dir") + "/drivers/" + chromeDriver);
                    System.setProperty("webdriver.chrome.driver", files.getAbsolutePath());

                    //To ignore the SSL certificates
                    DesiredCapabilities capabilitiesCH = DesiredCapabilities.chrome();
                    capabilitiesCH.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
                    capabilitiesCH.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

                    driver = new ChromeDriver(capabilitiesCH);
                    driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT, TimeUnit.SECONDS);
                    driver.manage().window().maximize();

                    log.info("Starting driver with chrome browser.");

                    break;
                case "firefox":
                    //To ignore the SSL certificates
                    DesiredCapabilities capabilitiesFF = DesiredCapabilities.firefox();
                    capabilitiesFF.setCapability("firefox.switches", Arrays.asList("--ignore-certificate-errors"));
                    capabilitiesFF.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

                    FirefoxProfile ffProfile = new FirefoxProfile();
                    ffProfile.setPreference("browser.download.folderList", 2);
                    ffProfile.setPreference("browser.download.manager.showWhenStarting", false);

                    //String testrunFolder = report.strFolderPathReporting.replace("/", "\\");
                    //ffProfile.setPreference("browser.download.dir", testrunFolder);
                    ffProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/excel, text/csv, text/comma-separated-values, application/csv, application/octet-stream");

                    //FirefoxBinary ffBinary = new FirefoxBinary(new File("C:/Program Files (x86)/Mozilla Firefox 22/firefox.exe"));
                    FirefoxBinary ffBinary = new FirefoxBinary();
                    ffBinary.setTimeout(java.util.concurrent.TimeUnit.SECONDS.toMillis(90));

                    driver = new FirefoxDriver(ffBinary, ffProfile, capabilitiesFF);
                    driver.manage().window().maximize();

                    log.info("Starting driver with firefox browser.");
                    break;
                default:
                    log.warn("No browser specified.");
                    break;
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

    //To stop webdriver.
    @Step("Ending driver.")
    public void stopDriver() {
        try {
            driver.quit();
            log.info("End driver.");
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

    //To capture the image of the current screen
    public void captureImage() {
        String tempImageFolder;
        try{
            tempImageFolder = globalTestRunFolder + "capturedImages/";
            if (!new File(tempImageFolder).exists()) {
                new File(tempImageFolder).mkdirs();
            }

            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(tempImageFolder + "img_" + new SimpleDateFormat("ddMMyyyyHHmmssSSS").format(Calendar.getInstance().getTime()) + ".png"));
        }catch(Exception ex){
            log.error("Java exception occurred : ", ex);
        }
    }

    //Soft Assert checkpoint.
    public void assertAll() {
        if (softAssert != null) {
            softAssert.assertAll();
            softAssert = null;
        }
    }

}
