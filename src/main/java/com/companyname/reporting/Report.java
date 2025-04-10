package com.companyname.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report {

    static ExtentSparkReporter extentSparkReporter;
    public static ExtentReports extent;
    public static ExtentReports extent2;
    public static ExtentTest extentLogger;
    public static ExtentTest extentLogger2;

    public static void createReport(ITestContext context) {

        extentSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "//test-output//Extent/report.html");
        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setDocumentTitle("DCS Smartcomm Validation Reports");
        extentSparkReporter.config().setReportName("DCS Smartcomm Validation Report");
        extentSparkReporter.config().setTheme(Theme.DARK);
        extentSparkReporter.config().enableTimeline(true);
        extentSparkReporter.config().setAutoCreateRelativePathMedia(true);
        extent = new ExtentReports();
        extent.attachReporter(extentSparkReporter);
        extent.setSystemInfo("Tester Name", System.getProperty("user.name"));
        extent.setSystemInfo("Project", "DMS-Test");
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        extentLogger = extent.createTest(context.getSuite().getName() + " " + context.getName());

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMdd_hh-mm-ss"));
        Path path = Paths.get(System.getProperty("user.dir"), "test-output/Extent/html", "Default Suite_" + currentDate + ".html");
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(path.toString());
        extent2 = new ExtentReports();
        extent2.attachReporter(htmlReporter);
        htmlReporter.config().setDocumentTitle("PDF Tests");
        htmlReporter.config().setReportName("PDF Tests Report");
        htmlReporter.config().setTheme(Theme.DARK);
    }

    public static void flushResults(ITestResult result, String testPath) {

        extentLogger.log(Status.INFO, "Description of the test case is : " + result.getMethod().getDescription());
        if (result.getStatus() == ITestResult.SUCCESS) {
            extentLogger.log(Status.PASS, result.getName() + " is PASSED");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            extentLogger.log(Status.FAIL, result.getName() + " is FAILED");
            extentLogger.log(Status.INFO, result.getThrowable().toString());
        }
        extent.flush();
    }

    public static void flushResults(ITestResult result) {
        extentLogger = extent.createTest(result.getName());
        extentLogger.log(Status.INFO, "Description of the test case is : " + result.getMethod().getDescription());

        if (result.getStatus() == ITestResult.SUCCESS) {
            extentLogger.log(Status.PASS, result.getName() + " is PASSED");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            extentLogger.log(Status.INFO, "Link to PDF: <a href='file:///" + System.getProperty("user.dir") +
                    "/test-output/"+ result.getName() +".pdf'>OutputPDFFile</a>");
            extentLogger.log(Status.INFO, System.getProperty("user.dir") + "/test-output/"+ result.getName() +".pdf");
            extentLogger.log(Status.FAIL, result.getName() + " is FAILED");
            extentLogger.log(Status.INFO, result.getThrowable().toString());
        }
        extent.flush();

        extentLogger2 = extent2.createTest(result.getName());
        extentLogger2.log(Status.INFO, "Description of the test case is : " + result.getMethod().getDescription());

        if (result.getStatus() == ITestResult.SUCCESS) {
            extentLogger2.log(Status.PASS, result.getName() + " is PASSED");
        } else if (result.getStatus() == ITestResult.FAILURE) {
            extentLogger2.log(Status.INFO, "Link to PDF: <a href='file:///" + System.getProperty("user.dir") +
                    "/test-output/"+ result.getName() +".pdf'>OutputPDFFile</a>");
            extentLogger2.log(Status.INFO, System.getProperty("user.dir") + "/test-output/"+ result.getName() +".pdf");
            extentLogger2.log(Status.FAIL, result.getName() + " is FAILED");
            extentLogger2.log(Status.INFO, result.getThrowable().toString());
        }
        extent2.flush();
    }
}
