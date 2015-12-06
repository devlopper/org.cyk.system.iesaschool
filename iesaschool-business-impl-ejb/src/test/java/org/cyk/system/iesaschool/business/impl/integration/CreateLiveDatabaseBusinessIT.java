package org.cyk.system.iesaschool.business.impl.integration;

import org.cyk.utility.common.Constant;

public class CreateLiveDatabaseBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	installApplication();
		rootDataProducerHelper.exportDatabase(Boolean.FALSE,Constant.EMPTY_STRING);
    	System.exit(0);
    }

}
