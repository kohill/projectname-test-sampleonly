package com.companyname.common.page;

import com.companyname.config.Browser;
import com.companyname.test.pagedriver.PageDriver;
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
