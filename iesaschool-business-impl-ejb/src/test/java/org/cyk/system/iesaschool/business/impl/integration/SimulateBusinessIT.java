package org.cyk.system.iesaschool.business.impl.integration;

import javax.inject.Inject;

import org.cyk.system.school.business.impl.SchoolBusinessTestHelper;


public class SimulateBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Inject private SchoolBusinessTestHelper schoolBusinessTestHelper;
    
    @Override
    protected void businesses() {
    	installApplication();
    	
    	schoolBusinessTestHelper.simulate(3, 3, 1,1, Boolean.FALSE,Boolean.FALSE);
    }
    
}
