package com.healthedge.app.flow;

import com.healthedge.app.providers.PageProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomePageFlow extends AbstractFlow {

    private static final Logger LOG = LogManager.getLogger(HomePageFlow.class);

    public HomePageFlow(PageProvider pageProvider) {
        super(pageProvider);
    }
}
