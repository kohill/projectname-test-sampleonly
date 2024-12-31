package com.healthedge.reporting;

import com.aventstack.extentreports.Status;
import com.healthedge.config.Browser;
import com.healthedge.config.BrowserListener;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;

import java.io.File;
import java.io.IOException;

public class ReportListener extends BrowserListener implements ITestListener, IInvokedMethodListener {

    private static Logger LOG = LogManager.getLogger(ReportListener.class);

    @Override
    public void onStart(ITestContext context) {
        ReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        logTestMessage(result, "STARTED");
        ReportTestManager.createTest(result.getTestContext().getSuite().getName() + " " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logTestMessage(result, "PASSED");
        ReportTestManager.getTest().log(Status.PASS, result.getName() + " is a success");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logTestMessage(result, "FAILED");
        ReportTestManager.getTest().log(Status.FAIL, result.getName() + " is a FAILURE");
        ReportTestManager.getTest().log(Status.INFO, result.getThrowable().toString());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logTestMessage(result, "SKIPPED");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logTestMessage(result, "TEST FAILED WITHIN SUCCESS PERCENTAGE");
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportTestManager.endTest();
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if(testResult.getStatus() == ITestResult.FAILURE && Browser.isScreenshotEnabled() && Browser.isInitialized()) {
            try {
                ReportTestManager.getTest().addScreenCaptureFromPath(takeScreenshot(testResult), "test title");
            } catch (Exception e) {
                LOG.error("Unable to add screenshot for test: " + testResult.getTestClass().getName() + " ::: " + testResult.getName());
            }
        }
    }

    private void logTestMessage(ITestResult result, String msgType) {
        if (result.getParameters() != null && result.getParameters().length > 0 && result.getParameters()[0] != null) {
            LOG.info("TEST {}: {}.{} {}", msgType, result.getTestClass().getName(), result.getName(), result.getParameters());
        } else {
            LOG.info("TEST {}: {}.{} {}", msgType, result.getTestClass().getName(), result.getName());
        }
    }

    public static String takeScreenshot(ITestResult result) {
        TakesScreenshot takesScreenshot = ((TakesScreenshot) Browser.get().getWebDriver());
        File screenshot = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String filePathName = "screenshots/" + result.getName() + "_" + Thread.currentThread().getId() + "_" + System.currentTimeMillis() + ".png";
        try {
            FileUtils.copyFile(screenshot, new File(filePathName));
        } catch (IOException e) {
            LOG.error("Unable to capture screenshot for test: " + result.getTestClass().getName() + " ::: " + result.getName());
        }
        return filePathName;
    }
}
