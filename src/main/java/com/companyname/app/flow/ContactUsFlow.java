package com.companyname.app.flow;

import com.companyname.app.providers.PageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactUsFlow extends AbstractFlow {

    private static final Logger LOG = LogManager.getLogger(ContactUsFlow.class);

    public ContactUsFlow(PageProvider pageProvider) {
        super(pageProvider);
    }

}
