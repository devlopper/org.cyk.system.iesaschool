package org.cyk.system.iesaschool.business.impl.integration;

public class ApplicationSetupBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Override
    protected void businesses() {
    	installApplication();
    	System.exit(0);
    }

    
    

}
