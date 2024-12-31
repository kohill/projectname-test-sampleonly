package com.healthedge.app.flow;

import com.healthedge.common.flow.BaseFlow;
import com.healthedge.app.providers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractFlow extends BaseFlow {

    private static final Logger LOG = LogManager.getLogger(AbstractFlow.class);

    protected AbstractFlow(PageProvider pageProvider) {
        super(pageProvider);
    }

}
