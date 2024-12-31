package com.healthedge.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ReportTestManager {

    public static ExtentReports extent = ReportManager.getInstance();
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    public static synchronized void endTest() {
        extent.flush();
    }


    public static synchronized ExtentTest createTest(String name) {

        ExtentTest test = extent.createTest(name);
        return extentTestMap.put((int) Thread.currentThread().getId(), test);
    }
}
