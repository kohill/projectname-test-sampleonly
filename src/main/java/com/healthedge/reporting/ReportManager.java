package com.healthedge.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.healthedge.config.props.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportManager {

    private static final Logger LOG = LogManager.getLogger(ReportManager.class);
    static ExtentSparkReporter extentSparkReporter;
    public static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            return createInstance();
        }
        LOG.debug("Instance already created");
        return extent;
    }

    public static synchronized ExtentReports createInstance() {
        extentSparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "//test-output//Extent/report.html");
        extentSparkReporter.config().setEncoding("utf-8");
        extentSparkReporter.config().setDocumentTitle("Automation Report");
        extentSparkReporter.config().setReportName(PropertyReader.getProperty("extent.reportname"));
        extentSparkReporter.config().setTheme(Theme.DARK);
        extentSparkReporter.config().enableTimeline(true);
        extentSparkReporter.config().setAutoCreateRelativePathMedia(true);
        extent = new ExtentReports();
        extent.attachReporter(extentSparkReporter);
        extent.setSystemInfo("Tester Name", System.getProperty("user.name"));
        extent.setSystemInfo("Project", PropertyReader.getProperty("extent.projectname"));
        extent.setSystemInfo("Operating System", System.getProperty("os.name"));
        return extent;
    }
}
