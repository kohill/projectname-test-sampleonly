package com.companyname.common;

import com.companyname.app.providers.FlowProvider;
import com.companyname.app.providers.PageProvider;
import com.companyname.exceptions.ProviderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public abstract class Provider {

    protected static Logger LOG = LogManager.getLogger(Provider.class);

    protected FlowProvider flowProvider;
    protected PageProvider pageProvider;

    protected Provider() {
    }

    protected <T> T getProviderInstance(T obj, Class<T> clazz) {
        try {
            obj = clazz.getConstructor(FlowProvider.class).newInstance(flowProvider);
            return obj;
        } catch (ProviderException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOG.error(e.getMessage(), e);
            throw new ProviderException("Unable to instantiate provider instance, check constructor");
        }
    }
}
