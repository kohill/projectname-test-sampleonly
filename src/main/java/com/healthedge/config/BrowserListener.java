package com.healthedge.config;

import com.healthedge.config.props.PropertyReader;
import org.testng.ITestResult;

public class BrowserListener {

    public void closeBrowser() {
    }

    protected String getBuildName(ITestResult result) {
        String envName = PropertyReader.getProperty("env.name", "No_Team_Name");
        return envName.concat(":").concat(getBuildName(result));
    }
}
