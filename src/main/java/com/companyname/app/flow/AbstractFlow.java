package com.companyname.app.flow;

import com.companyname.common.flow.BaseFlow;
import com.companyname.app.providers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractFlow extends BaseFlow {

    private static final Logger LOG = LogManager.getLogger(AbstractFlow.class);

    protected AbstractFlow(PageProvider pageProvider) {
        super(pageProvider);
    }

}
