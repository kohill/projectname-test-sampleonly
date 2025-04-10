package com.companyname.app.flow;

import com.companyname.app.providers.PageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AboutUsFlow extends AbstractFlow {

    private static final Logger LOG = LogManager.getLogger(AboutUsFlow.class);

    public AboutUsFlow(PageProvider pageProvider) {
        super(pageProvider);
    }

}
