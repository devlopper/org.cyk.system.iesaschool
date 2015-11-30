package org.cyk.system.iesaschool.business.impl.integration;

import javax.inject.Inject;

import org.cyk.system.school.business.impl.SchoolBusinessTestHelper;
import org.cyk.system.school.business.impl.SchoolBusinessTestHelper.SchoolBusinessSimulationParameters;


public class SimulateBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;

    @Inject private SchoolBusinessTestHelper schoolBusinessTestHelper;
    
    @Override
    protected void businesses() {
    	installApplication();
    	schoolBusinessTestHelper.setCoefficientApplied(Boolean.FALSE);
    	
    	SchoolBusinessSimulationParameters parameters = new SchoolBusinessSimulationParameters();

    	parameters.setGeneratedClassroomSessionCountByLevel(12);
    	parameters.getClassroomSessionDivisionIndexes().add(1);
    	schoolBusinessTestHelper.simulate(parameters);
    }
    
}
