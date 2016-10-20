package org.cyk.system.iesaschool.business.impl.integration;

import org.cyk.system.root.business.impl.BusinessInterfaceLocator;
import org.cyk.system.root.model.AbstractIdentifiable;
import org.cyk.system.school.business.impl.session.LevelBusinessImpl;
import org.cyk.system.school.business.impl.session.LevelTimeDivisionBusinessImpl;
import org.cyk.system.school.model.session.ClassroomSession;
import org.cyk.system.school.model.session.Level;
import org.cyk.system.school.model.session.LevelGroup;
import org.cyk.system.school.model.session.LevelTimeDivision;

public class AutoPropertySetBusinessIT extends AbstractBusinessIT {

    private static final long serialVersionUID = -6691092648665798471L;
    
    @Override
    protected void businesses() {
    	LevelTimeDivisionBusinessImpl.PROPERTY_VALUE_TOKENS_CONCATENATE_WITH_TIMEDIVISIONTYPE = Boolean.FALSE;
    	LevelBusinessImpl.PROPERTY_VALUE_TOKENS_CONCATENATE_WITH_GROUP_LEVELNAME_SPECIALITY = Boolean.FALSE;
    	installApplication();
    	print(LevelGroup.class);
    	print(Level.class);
    	print(LevelTimeDivision.class);
    	print(ClassroomSession.class);
    	System.exit(0);
    }
    
    private <IDENTIFIABLE extends AbstractIdentifiable> void print(Class<IDENTIFIABLE> aClass){
    	for(IDENTIFIABLE identifiable : inject(BusinessInterfaceLocator.class).injectTyped(aClass).findAll())
    		System.out.println(aClass.getSimpleName()+" : "+identifiable.getCode()+" , "+identifiable.getName());
    }
    
}
