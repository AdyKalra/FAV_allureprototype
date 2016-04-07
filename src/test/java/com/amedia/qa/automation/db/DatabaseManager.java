package com.amedia.qa.automation.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * Created by voravuthboonchai on 3/31/2016 AD.
 */
public class DatabaseManager {

    private final String dbServer = "jdbc:mysql://192.168.50.48:3306/automation_results";
    private final String dbUsername = "root";
    private final String dbPassword = "pingu123";
    private String sql = "";
    private Connection con = null;
    Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    public DatabaseManager() {

    }

    //To open and connect to DB.
    private Connection openDBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(dbServer, dbUsername, dbPassword);
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
        return con;
    }

    //To close the DB connection.
    private void closeDBConnection() {
        try {
            con.close();
            con = null;
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

    //Record test execution results into database.
    public void recordTestExecutionResults(String testSuiteName, String testStatus, int testNo, int testPassedNo, int testFailedNo, int testSkippedNo, String executionDateTime, String filePath) {
        try {
            sql = "INSERT INTO TestRuns(TR_TestSuiteName, TR_TestStatus, TR_TestCaseNo, TR_TestPassedNo, TR_TestFailedNo, TR_TestSkippedNo, TR_ExecutionDateTime, TR_FilePath) " +
                    "VALUES ('" + testSuiteName + "', '" + testStatus + "', " + testNo + ", " + testPassedNo + ", " + testFailedNo + ", " + testSkippedNo + ", '" + executionDateTime + "', '" + filePath + "')";

            //System.out.println(sql);
            con = openDBConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            closeDBConnection();
        } catch (Exception ex) {
            log.error("Java exception occurred : ", ex);
        }
    }

}
