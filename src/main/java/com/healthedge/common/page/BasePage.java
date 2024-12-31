package com.healthedge.common.page;

import com.healthedge.config.Browser;
import com.healthedge.test.pagedriver.PageDriver;
import org.openqa.selenium.*;

public abstract class BasePage extends PageDriver{
    PageDriver pageDriver;

    public BasePage() {
        super(Browser.getDriver());
    }

    public PageDriver BasePage() {
       return Browser.get();
    }

    public PageDriver getPageDriver() {
        return Browser.get();
    }

    public WebDriver getWebDriver() {
        return getPageDriver().getWebDriver();
    }
}
