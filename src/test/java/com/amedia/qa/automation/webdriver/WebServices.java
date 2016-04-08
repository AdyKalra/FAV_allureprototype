package com.amedia.qa.automation.webdriver;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.with;

/**
 * Created by voravuthboonchai on 3/29/2016 AD.
 */
public class WebServices extends Framework {

    private String tempFileName = "";
    private String tempFileMask1 = "";
    private String tempFileMask2 = "";
    private String tempFolder;
    final String ignoreList = System.getProperty("user.dir") + "/src/test/resources/ignoreList.txt";

    Logger log = LoggerFactory.getLogger(WebServices.class);

    public WebServices() {

    }

    //To check the local temp folder is created or not ?
    private void checkLocalTempFolderIsCreated() {
        tempFolder = globalTestRunFolder + "temp/file";
        if (!new File(tempFolder).exists()) {
            new File(tempFolder).mkdirs();
        }
    }

    @Step("Verify value of GET method from file : {0}")
    //To verify the value that return from webservice by using GET method
    public void verifyValueOfGetMethod(String testFile) {
        softAssert = new SoftAssert();
        ArrayList<String> arrayData;
        String[] extractData;
        String actualValue = "";
        String uri, id, expectedValue, contentType;
        try {
            arrayData = readFileToArrayList(testFile);
            if (!arrayData.isEmpty()) {
                for (String line : arrayData) {
                    extractData = line.split("::");

                    uri = extractData[0].trim();
                    id = extractData[1].trim();
                    expectedValue = extractData[2].trim();

                    if (!uri.isEmpty() && !id.isEmpty() && !expectedValue.isEmpty()) {

                        contentType = new RestAssured().get(uri).getContentType();

                        if (contentType.toLowerCase().contains("json")) {
                            actualValue = get(uri).path(id).toString().replace("[", "").replace("]", "");
                        } else if (contentType.toLowerCase().contains("xml")) {
                            actualValue = with().get(uri).path(id);
                        }
                        softAssert.assertEquals(actualValue, expectedValue);
                        if (actualValue.equals(expectedValue)) {
                            log.info("PASSED ["+ contentType +"] : The actual value (" + actualValue + ") is matched with the expected value (" + expectedValue + ") at path : " + id);
                        } else {
                            log.warn("FAILED ["+ contentType +"] : The actual value (" + actualValue + ") does not matched with the expected value (" + expectedValue + ") at path : " + id);
                        }
                    }
                }
                assertAll();
            } else {
                log.error("Unable to read file : " + testFile);
                softAssert.fail("Unable to read file : " + testFile);
                assertAll();
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }

    }

    @Step("Verify value of POST method from file : {0}")
    //Verify the output of POST method by receiving the POST command in the file and
    // get the return result to compare with the expected result file
    public void verifyValueOfPostMethod(String testFile) {
        softAssert = new SoftAssert();
        ArrayList<String> arrayData;
        String[] extractData;
        String uri;
        String postFile;
        String expectedFile;
        String extension;
        String bodyTxt;
        Response response;

        try {
            checkLocalTempFolderIsCreated();
            tempFileMask1 = tempFolder + "/tempFileMask1_" + new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime()) + ".txt";
            tempFileMask2 = tempFolder + "/tempFileMask2_" + new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime()) + ".txt";

            arrayData = readFileToArrayList(testFile);
            if (!arrayData.isEmpty()) {
                for (String line : arrayData) {
                    extractData = line.split("::");

                    uri = extractData[0].trim();
                    postFile = extractData[1].trim();
                    expectedFile = extractData[2].trim();

                    extension = postFile.toLowerCase().substring(postFile.length()-4, postFile.length());

                    switch (extension) {
                        case "json":
                            bodyTxt = readFileToString(postFile);
                            response = given().contentType(ContentType.JSON).body(bodyTxt).when().post(uri);
                            writeStringToText(response.asString());

                            fileMaskUp(expectedFile, ignoreList, tempFileMask1);
                            fileMaskUp(tempFileName, ignoreList, tempFileMask2);

                            compare2JsonFiles(tempFileMask1, tempFileMask2);

                            //Delete temp files after comparison
                            new File(tempFileMask1).delete();
                            new File(tempFileMask2).delete();
                            break;
                        case ".xml":
                            bodyTxt = readFileToString(postFile);
                            response = given().contentType(ContentType.XML).body(bodyTxt).when().post(uri);
                            writeStringToText(response.asString());

                            fileMaskUp(expectedFile, ignoreList, tempFileMask1);
                            fileMaskUp(tempFileName, ignoreList, tempFileMask2);

                            compare2XmlFiles(tempFileMask1, tempFileMask2);

                            //Delete temp files after comparison
                            new File(tempFileMask1).delete();
                            new File(tempFileMask2).delete();
                            break;
                        default:
                            log.warn("File format is wrong. Only XML and JSON format are supported.");
                            softAssert.fail("File format is wrong. Only XML and JSON format are supported.");
                            assertAll();
                            break;
                    }
                }
            } else {
                log.error("Unable to read file : " + testFile);
                softAssert.fail("Unable to read file : " + testFile);
                assertAll();
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //Return the single value to String from GET method.
    public String getValueFromGetMethod(String uri, String id) {
        String contentType;
        String output = null;
        try {
            contentType = new RestAssured().get(uri).getContentType().toLowerCase();

            if (contentType.toLowerCase().contains("json")) {
                output = get(uri).path(id).toString().replace("[", "").replace("]", "");
            } else if (contentType.toLowerCase().contains("xml")) {
                output = with().get(uri).path(id);
            } else {
                log.warn("ContentType is not supported. Please check your URI.");
            }
            return output;
        } catch (Exception ex) {
            output = null;
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
        return output;
    }

    //Return the single value to string from POST method my input file.
    public String getValueFromPostMethod(String filename, String uri) {
        Response response;
        String bodyTxt, format;
        String output = null;
        try {
            bodyTxt = readFileToString(filename);
            format = filename.toLowerCase().substring(filename.length()-4, filename.length());

            switch (format.toLowerCase().trim()) {
                case "json":
                    response = given().contentType(ContentType.JSON).body(bodyTxt).when().post(uri);
                    output = response.asString();
                    break;
                case "xml":
                    response = given().contentType(ContentType.XML).body(bodyTxt).when().post(uri);
                    output = response.asString();
                    break;
                default:
                    log.warn("File format is wrong. Only XML and JSON format are supported.");
                    break;
            }
            return output;
        } catch (Exception ex) {
            output = null;
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
        return output;
    }

    //To read text file and add to array list for further using
    private ArrayList<String> readFileToArrayList(String filename) {
        String currentLine;
        BufferedReader br = null;
        ArrayList<String> arrayData = null;
        try {
            arrayData = new ArrayList<>();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
            while ((currentLine = br.readLine()) != null) {
                if (!currentLine.isEmpty() && !currentLine.equals(" ")) {
                    arrayData.add(currentLine);
                }
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException io) {
                log.error("Java exception occurred : ", io);
            }
        }
        return arrayData;
    }

    //To read content of the file and store as string
    private String readFileToString(String filename) {
        BufferedReader br = null;
        StringBuilder sb = null;
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
            sb = new StringBuilder();
            line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException io) {
                log.error("Java exception occurred : ", io);
            }
        }
        return sb.toString();
    }

    //To write string to text file
    private void writeStringToText(String textToWrite) {
        BufferedWriter out;
        try {
            checkLocalTempFolderIsCreated();
            tempFileName = tempFolder + "/temp_" + new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Calendar.getInstance().getTime()) + ".txt";
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFileName), Charset.forName("UTF-8")));
            out.write(textToWrite);
            out.close();
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

    //Comparing 2 json text files
    private void compare2JsonFiles(String expectedFile, String actualFile) {
        ObjectMapper jsonMapper;
        try {
            jsonMapper = new ObjectMapper();
            jsonMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

            JsonNode expectedJsonNode = jsonMapper.readTree(new File(expectedFile));
            String expectedJson = expectedJsonNode.toString();

            JsonNode actualJsonNode = jsonMapper.readTree(new File(actualFile));
            String actualJson = actualJsonNode.toString();

            softAssert.assertEquals(actualJson, expectedJson);
            if (expectedJson.equals(actualJson)) {
                log.info("PASSED : The comparison is matched.");
            } else {
                log.warn("FAILED : The actual result doesn't match with the expectation. Please check in the file : " + tempFileName);
            }
            assertAll();
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //Comparing 2 xml text files
    private void compare2XmlFiles (String expectedFile, String actualFile) {
        List<?> allDifferences;
        try {
            XMLUnit.setIgnoreWhitespace(true);
            DetailedDiff diff = new DetailedDiff(XMLUnit.compareXML(readFileToString(expectedFile), readFileToString(actualFile)));

            if (diff.getAllDifferences().size() == 0) {
                log.info("PASSED : The comparison is matched.");
            } else {
                allDifferences = diff.getAllDifferences();
                Object[] array = allDifferences.toArray();

                softAssert.assertFalse(true, "FAILED : The actual result doesn't match with the expectation. Please check in the file : " + actualFile);
                log.warn("FAILED : The actual result doesn't match with the expectation. Please check in the file : " + actualFile);

                for (int i = 0; i < array.length; i++) {
                    log.warn(array[i].toString().replaceAll("\\s+", " "));
                }

            }
            assertAll();
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
            Assert.fail(ex.toString());
        }
    }

    //To mask up some text in file before comparing the file
    private void fileMaskUp(String fileToMask, String ignoreList, String tempFileMask) {
        String currentLine;
        String txtIgnoreList;
        String[] arrayIgnoreList;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        Pattern pattern;
        Matcher matcher;
        try {
            txtIgnoreList = readFileToString(ignoreList);
            arrayIgnoreList = txtIgnoreList.split(";");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToMask), Charset.forName("UTF-8")));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFileMask), Charset.forName("UTF-8")));

            for (String regEx : arrayIgnoreList) {
                while ((currentLine = reader.readLine()) != null) {
                    if (!regEx.isEmpty() && !regEx.equals("")) {
                        pattern = Pattern.compile(regEx);
                        matcher = pattern.matcher(currentLine);

                        if (matcher.find()) {
                            currentLine = currentLine.replaceAll(regEx, "");
                        }

                        writer.write(currentLine.replaceAll("\\s+", " "));
                    }
                }
            }
            reader.close();
            writer.close();
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException io) {
                log.error("Java exception occurred : ", io);
            }
        }
    }

}
