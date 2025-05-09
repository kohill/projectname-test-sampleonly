package com.companyname.app.providers;

import com.companyname.common.Provider;
import com.companyname.exceptions.ProviderException;
import com.companyname.app.flow.LoginFlow;
import com.companyname.app.flow.AboutUsFlow;
import com.companyname.app.flow.ContactUsFlow;
import com.companyname.app.flow.HomePageFlow;

import java.lang.reflect.InvocationTargetException;

public final class FlowProvider extends Provider {

    private FlowProvider flowProvider;

    private LoginFlow loginFlow;

    private AboutUsFlow aboutUsFlow;
    private ContactUsFlow homePolicyFlow;
    private HomePageFlow pupPolicyFlow;

    public FlowProvider() {
    }

    public LoginFlow getLoginFlow() {
        loginFlow = getProviderInstance(loginFlow, LoginFlow.class);
        return loginFlow;
    }

    public ContactUsFlow getHomePolicyFlow() {
        if (homePolicyFlow == null) {
            homePolicyFlow = new ContactUsFlow(pageProvider);
        }
        return homePolicyFlow;
    }

    public HomePageFlow getPupPolicyFlow() {
        if (pupPolicyFlow == null) {
            pupPolicyFlow = new HomePageFlow(pageProvider);
        }
        return pupPolicyFlow;
    }

    @Override
    protected <T> T getProviderInstance(T obj, Class<T> clazz) {
        try {
            obj = clazz.getConstructor(FlowProvider.class)
                    .newInstance(flowProvider);
            return obj;
        } catch (ProviderException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOG.error(e.getMessage(), e);
            throw new ProviderException("Unable to instantiate provider instance, check constructor");
        }
    }
}
