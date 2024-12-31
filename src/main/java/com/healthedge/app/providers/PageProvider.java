package com.healthedge.app.providers;

import com.healthedge.app.page.main.*;
import com.healthedge.app.page.common.*;
import com.healthedge.common.Provider;
import com.healthedge.exceptions.ProviderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class PageProvider extends Provider {

    private static Logger LOG = LogManager.getLogger(PageProvider.class);

    private LoginPage loginPage;
    private HomePage cancellationNoticePage;
    private AboutUsPage cancellationPage;

    protected PageProvider() {
        super();
    }

    public LoginPage loginPage() {
        loginPage = getProviderInstance(loginPage, LoginPage.class);
        return loginPage;
    }

    public HomePage cancellationNoticePage() {
        cancellationNoticePage = getProviderInstance(cancellationNoticePage, HomePage.class);
        return cancellationNoticePage;
    }

    public AboutUsPage cancellationPage() {
        cancellationPage = getProviderInstance(cancellationPage, AboutUsPage.class);
        return cancellationPage;
    }

    @Override
    protected <T> T getProviderInstance(T obj, Class<T> clazz) {
        try {
            obj = clazz.getConstructor(PageProvider.class)
                    .newInstance(pageProvider);
            return obj;
        } catch (ProviderException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOG.error(e.getMessage(), e);
            throw new ProviderException("Unable to instantiate provider instance, check constructor");
        }
    }
}
