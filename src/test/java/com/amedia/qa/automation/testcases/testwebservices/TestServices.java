package com.amedia.qa.automation.testcases.testwebservices;

import com.amedia.qa.automation.testcases.BaseTest;

import org.testng.annotations.*;

/**
 * Created by voravuthboonchai on 3/3/2016 AD.
 */
public class TestServices extends BaseTest {

    @Test(description = "Test REST service.")
    public void testRESTService() {
        try {
            //String aaa = service.getValueFromGetMethod("http://bearing.dev.abctech-thailand.com/api/frontier/v1/companies/2691314", "fields.id[0]");
            //String bbb = service.getValueFromPostMethod(System.getProperty("user.dir") + "/src/test/resources/testfiles/postFile.json", "http://ari:9066/api/garudaimport/v1/rest/NTB/www.rumbarapporten.no/sync");

            service.verifyValueOfGetMethod(System.getProperty("user.dir") + "/src/test/resources/testsources/TestServices/Test02.txt");
            service.verifyValueOfGetMethod(System.getProperty("user.dir") + "/src/test/resources/testsources/TestServices/Test03.txt");
            service.verifyValueOfPostMethod(System.getProperty("user.dir") + "/src/test/resources/testsources/TestServices/Test01.txt");
            //service.verifyValueOfPostMethod("/Users/voravuthboonchai/Desktop/Test/TestFiles/Test04.txt");

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

}
