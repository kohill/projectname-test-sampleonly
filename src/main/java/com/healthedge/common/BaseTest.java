package com.healthedge.common;

import com.healthedge.app.flow.AboutUsFlow;
import com.healthedge.app.flow.ContactUsFlow;
import com.healthedge.app.flow.HomePageFlow;
import com.healthedge.app.flow.LoginFlow;
import com.healthedge.app.providers.FlowProvider;
import com.healthedge.app.providers.PageProvider;
import com.healthedge.config.Browser;
import com.healthedge.config.Properties;
import com.healthedge.config.props.PropertyReader;
import com.healthedge.reporting.ReportListener;
import com.healthedge.reporting.ReportTestManager;
import com.healthedge.test.pagedriver.Logs;
import com.healthedge.test.pagedriver.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

//@Listeners({BrowserListener.class})
public class BaseTest extends Provider implements ITestListener {

    protected AboutUsFlow aboutUsFlow;
    protected ContactUsFlow contactUsFlow;
    protected HomePageFlow homePageFlow;
    protected LoginFlow loginFlow;

    protected static final Logger LOG = LogManager.getLogger(BaseTest.class);
    private static final Boolean CLOSE_BROWSER_ON_FINISH = PropertyReader.getProperty(Properties.CLOSE_BROWSER_ON_FINISH, true);
    private static final String TEST_LOG_LEVEL = PropertyReader.getProperty(Properties.TEST_LOG_LEVEL, "info");
    private static final ThreadLocal<SessionManager> session = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void setSuiteConfigInfo() {
        Configurator.setRootLevel(Logs.getLevel(TEST_LOG_LEVEL));
    }

    @AfterSuite(alwaysRun = true)
    public void testSuiteTearDown() {
        if (CLOSE_BROWSER_ON_FINISH) {
            closeApp();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setTestConfigInfo(Method method, ITestContext context) {
        Browser.initialize(session.get());
    }

    @AfterMethod(alwaysRun = true)
    public void testMethodTearDown(ITestResult result) {
        closeApp();
    }

    public static void closeApp() {
        session.get().stop();
    }

    public void runSuitePreconditions() {
        LOG.info("No suite-level preconditions found, proceeding with test(s)");
    }

    public String getScreenshot(ITestResult result) throws Exception {
        String screenshotPath;
        screenshotPath = ReportListener.takeScreenshot(result);
        ReportTestManager.getTest().addScreenCaptureFromPath(screenshotPath);
        return screenshotPath;
    }

    /* --------------------------------------------  FLOWS -------------------------------------------- */

    public AboutUsFlow getAboutUsFlow() {
        aboutUsFlow = getProviderInstance(aboutUsFlow, AboutUsFlow.class);
        return aboutUsFlow;
    }

    public ContactUsFlow getContactUsFlow() {
        contactUsFlow = getProviderInstance(contactUsFlow, ContactUsFlow.class);
        return contactUsFlow;
    }

    public HomePageFlow getHomePageFlow() {
        homePageFlow = getProviderInstance(homePageFlow, HomePageFlow.class);
        return homePageFlow;
    }

    public LoginFlow getLoginFlow() {
        loginFlow = getProviderInstance(loginFlow, LoginFlow.class);
        return loginFlow;
    }


}
